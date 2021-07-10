package com.epam.esm.service.impl;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.mapper.Mapper;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.exception.InvalidParameterException;
import com.epam.esm.exception.NoSuchEntityException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final GiftCertificateRepository certificateRepository;
    private final UserRepository userRepository;

    private final Mapper<GiftCertificate, GiftCertificateDto> giftCertificateMapper;
    private final Mapper<Order, OrderDto> orderMapper;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            GiftCertificateRepository certificateRepository,
                            UserRepository userRepository,
                            Mapper<GiftCertificate, GiftCertificateDto> giftCertificateMapper,
                            Mapper<Order, OrderDto> orderMapper) {
        this.orderRepository = orderRepository;
        this.certificateRepository = certificateRepository;
        this.userRepository = userRepository;
        this.giftCertificateMapper = giftCertificateMapper;
        this.orderMapper = orderMapper;
    }

    @Override
    @Transactional
    public OrderDto create(OrderDto orderDto) {
        if (!userRepository.read(orderDto.getUserId()).isPresent()) {
            throw new NoSuchEntityException("user.not.found");
        }
        if (orderDto.getUserId() < 0) {
            throw new InvalidParameterException("user.invalid");
        }
        List<GiftCertificateDto> orderCertificates = orderDto.getCertificates();
        Iterator<GiftCertificateDto> iterator = orderCertificates.iterator();
        List<GiftCertificateDto> changedOrderCertificates = new ArrayList<>();
        while (iterator.hasNext()) {
            GiftCertificateDto giftCertificateDto = iterator.next();
            Optional<GiftCertificate> foundCertificate = certificateRepository.read(giftCertificateDto.getId());
            changedOrderCertificates.add(giftCertificateMapper.toDto(foundCertificate.orElseThrow(() -> new NoSuchEntityException("certificate.not.found"))));
        }
        orderDto = OrderDto.builder()
                .date(LocalDateTime.now())
                .certificates(changedOrderCertificates)
                .cost(calculateOrderCost(changedOrderCertificates))
                .build();
        Order order = orderRepository.create(orderMapper.toModel(orderDto));
        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto find(long id) {
        return null;
    }

    @Override
    public List<OrderDto> getUserOrders(long userId, Pageable pageable) {
        return null;
    }

    private BigDecimal calculateOrderCost(List<GiftCertificateDto> certificates) {
        BigDecimal cost = new BigDecimal(0);
        for (GiftCertificateDto certificate : certificates) {
            cost = cost.add(certificate.getPrice());
        }
        return cost;
    }


}
