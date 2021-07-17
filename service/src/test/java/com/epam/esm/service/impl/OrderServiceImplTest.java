package com.epam.esm.service.impl;

import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.repository.entity.GiftCertificate;
import com.epam.esm.repository.entity.Order;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.repository.entity.User;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.dto.mapper.Mapper;
import com.epam.esm.service.dto.mapper.impl.GiftCertificateMapper;
import com.epam.esm.service.dto.mapper.impl.OrderMapper;
import com.epam.esm.service.dto.mapper.impl.TagMapper;
import com.epam.esm.service.dto.mapper.impl.UserMapper;
import com.epam.esm.service.exception.InvalidParameterException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.validator.impl.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderServiceImplTest {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 50;

    private OrderDto orderDtoToCreate;
    private Order order;
    private List<GiftCertificateDto> giftCertificateDtoList;
    private GiftCertificateDto firstGiftCertificateDto;
    private User user;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private GiftCertificateRepository certificateRepository;
    @Mock
    private UserRepository userRepository;

    private Mapper<Tag, TagDto> tagMapper = new TagMapper(new ModelMapper());
    private Mapper<GiftCertificate, GiftCertificateDto> giftCertificateMapper = new GiftCertificateMapper(new ModelMapper(), tagMapper);
    private Mapper<Order, OrderDto> orderMapper = new OrderMapper(new ModelMapper(), giftCertificateMapper);
    private Mapper<User, UserDto> userMapper = new UserMapper(new ModelMapper());
    private UserValidator userValidator = new UserValidator();

    private OrderServiceImpl orderService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderServiceImpl(orderRepository, certificateRepository, userRepository, giftCertificateMapper, orderMapper, userValidator);
        firstGiftCertificateDto = GiftCertificateDto.builder()
                .id(1)
                .name("cert 1")
                .description("desc 1")
                .createDate(LocalDateTime.now())
                .lastUpdateDate(LocalDateTime.now())
                .price(BigDecimal.valueOf(10))
                .build();
        user = new User(1L, "user");
        giftCertificateDtoList = Arrays.asList(firstGiftCertificateDto);
        orderDtoToCreate = OrderDto.builder()
                .certificates(giftCertificateDtoList)
                .build();
        order = orderMapper.toModel(orderDtoToCreate);
    }

    @Test
    void testShouldCreate() {
        orderDtoToCreate.setUser(userMapper.toDto(user));
        order.setUser(user);
        when(orderRepository.create(any())).thenReturn(order);
        when(userRepository.read(1L)).thenReturn(Optional.ofNullable(user));
        GiftCertificate certificate = giftCertificateMapper.toModel(firstGiftCertificateDto);
        when(certificateRepository.read(anyLong())).thenReturn(Optional.of(certificate));
        OrderDto createdOrder = orderService.create(orderDtoToCreate);
        assertEquals(orderDtoToCreate, createdOrder);
        verify(orderRepository).create(any());
    }

    @Test
    void testCreateShouldThrowNoSuchEntityExceptionUserNotFound() {
        assertThrows(NoSuchEntityException.class, () -> orderService.create(orderDtoToCreate));
    }

    @Test
    void testCreateShouldThrowNoSuchEntityExceptionCertificateNotFound() {
        when(certificateRepository.read(anyLong())).thenReturn(Optional.empty());
        assertThrows(NoSuchEntityException.class, () -> orderService.create(orderDtoToCreate));
    }

    @Test
    void testShouldFindAllByUserId() {
        when(userRepository.read(1L)).thenReturn(Optional.of(user));
        when(orderRepository.getUserOrders(anyLong(), any())).thenReturn(Collections.singletonList(order));
        assertEquals(orderService.getUserOrders(user.getId(), DEFAULT_PAGE, DEFAULT_PAGE_SIZE), Collections.singletonList(orderDtoToCreate));
    }

    @Test
    void testFindAllByUserIdShouldThrowInvalidParameterException() {
        when(userRepository.read(1L)).thenReturn(Optional.of(user));
        assertThrows(InvalidParameterException.class, () -> orderService.getUserOrders(user.getId(), -3, 4));
    }

    @Test
    void testShouldFindOrderByUserId() {
        when(orderRepository.read(orderDtoToCreate.getId())).thenReturn(Optional.of(order));
        when(userRepository.read(user.getId())).thenReturn(Optional.of(user));
        when(orderRepository.getUserOrders(anyLong(), any())).thenReturn(Collections.singletonList(order));
        assertEquals(orderDtoToCreate, orderService.findByUserId(user.getId(), orderDtoToCreate.getId()));
    }

    @Test
    void testFindOrderByUserIdShouldThrowNoSuchEntityExceptionUserNotFound() {
        when(orderRepository.read(orderDtoToCreate.getId())).thenReturn(Optional.of(order));
        when(userRepository.read(user.getId())).thenReturn(Optional.empty());
        assertThrows(NoSuchEntityException.class, () -> orderService.findByUserId(user.getId(), orderDtoToCreate.getId()));
    }

    @Test
    void testFindOrderByUserIdShouldThrowNoSuchEntityExceptionOrderNotFound() {
        when(orderRepository.read(orderDtoToCreate.getId())).thenReturn(Optional.empty());
        assertThrows(NoSuchEntityException.class, () -> orderService.findByUserId(user.getId(), orderDtoToCreate.getId()));
    }
}