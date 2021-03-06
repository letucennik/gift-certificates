package com.epam.esm.service.impl;

import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.repository.entity.GiftCertificate;
import com.epam.esm.repository.entity.Order;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.dto.mapper.Mapper;
import com.epam.esm.service.exception.InvalidParameterException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.validator.impl.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    public static final String USER_NOT_FOUND = "user.not.found";
    public static final String ORDER_NOT_FOUND = "order.not.found";

    private final OrderRepository orderRepository;
    private final GiftCertificateRepository certificateRepository;
    private final UserRepository userRepository;

    private final Mapper<GiftCertificate, GiftCertificateDto> giftCertificateMapper;
    private final Mapper<Order, OrderDto> orderMapper;

    private final UserValidator userValidator;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            GiftCertificateRepository certificateRepository,
                            UserRepository userRepository,
                            Mapper<GiftCertificate, GiftCertificateDto> giftCertificateMapper,
                            Mapper<Order, OrderDto> orderMapper,
                            UserValidator userValidator) {
        this.orderRepository = orderRepository;
        this.certificateRepository = certificateRepository;
        this.userRepository = userRepository;
        this.giftCertificateMapper = giftCertificateMapper;
        this.orderMapper = orderMapper;
        this.userValidator = userValidator;
    }

    @Override
    @Transactional
    public OrderDto create(OrderDto orderDto) {
        validateUser(orderDto.getUser());
        List<GiftCertificateDto> orderCertificates = orderDto.getCertificatesDto();
        if (orderCertificates == null || orderCertificates.isEmpty()) {
            throw new InvalidParameterException("orders.empty");
        }
        Iterator<GiftCertificateDto> iterator = orderCertificates.iterator();
        List<GiftCertificateDto> changedOrderCertificates = new ArrayList<>();
        while (iterator.hasNext()) {
            GiftCertificateDto giftCertificateDto = iterator.next();
            Optional<GiftCertificate> foundCertificate = certificateRepository.findById(giftCertificateDto.getId());
            changedOrderCertificates.add(giftCertificateMapper.toDto(foundCertificate.orElseThrow(() -> new NoSuchEntityException("certificate.not.found"))));
        }
        orderDto = OrderDto.builder()
                .user(orderDto.getUser())
                .date(LocalDateTime.now())
                .certificatesDto(changedOrderCertificates)
                .cost(calculateOrderCost(changedOrderCertificates))
                .build();
        Order order = orderRepository.save(orderMapper.toModel(orderDto));
        return orderMapper.toDto(order);
    }

    @Override
    @Transactional
    public OrderDto findByUserId(long userId, long orderId) {
        userRepository.findById(userId).orElseThrow(() -> new NoSuchEntityException(USER_NOT_FOUND));
        Order foundOrder = orderRepository.findDistinctByUserIdAndId(userId, orderId).orElseThrow(() -> new NoSuchEntityException(ORDER_NOT_FOUND));
        return orderMapper.toDto(foundOrder);
    }


    @Override
    @Transactional
    public List<OrderDto> getUserOrders(long userId, int page, int size) {
        if (!userRepository.findById(userId).isPresent()) {
            throw new NoSuchEntityException(USER_NOT_FOUND);
        }
        Pageable pageRequest;
        try {
            pageRequest = PageRequest.of(page, size);
        } catch (IllegalArgumentException e) {
            throw new InvalidParameterException("pagination.invalid");
        }
        return orderRepository.findDistinctByUserId(userId, pageRequest)
                .stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    private BigDecimal calculateOrderCost(List<GiftCertificateDto> certificates) {
        BigDecimal cost = new BigDecimal(0);
        for (GiftCertificateDto certificate : certificates) {
            cost = cost.add(certificate.getPrice());
        }
        return cost;
    }

    private void validateUser(UserDto userDto) {
        if (!userValidator.isNotNull(userDto)) {
            throw new NoSuchEntityException(USER_NOT_FOUND);
        }
        if (!userValidator.isIdValid(userDto.getId())) {
            throw new InvalidParameterException("user.invalid");
        }
        if (!userRepository.findById(userDto.getId()).isPresent()) {
            throw new NoSuchEntityException(USER_NOT_FOUND);
        }
    }


}
