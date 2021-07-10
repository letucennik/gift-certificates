package com.epam.esm.service.impl;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.mapper.Mapper;
import com.epam.esm.dto.mapper.impl.GiftCertificateMapper;
import com.epam.esm.dto.mapper.impl.OrderMapper;
import com.epam.esm.dto.mapper.impl.TagMapper;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderServiceImplTest {

    private OrderDto orderDtoToCreate;
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

    private Mapper<Order, OrderDto> orderMapper = new OrderMapper(new ModelMapper());

    private OrderServiceImpl orderService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderServiceImpl(orderRepository, certificateRepository, userRepository, giftCertificateMapper, orderMapper);
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
                .userId(user.getId())
                .build();
    }

    @Test
    void testShouldCreate() {
        Order order = orderMapper.toModel(orderDtoToCreate);
        when(orderRepository.create(any())).thenReturn(order);
        when(userRepository.read(1L)).thenReturn(Optional.ofNullable(user));
        GiftCertificate certificate = giftCertificateMapper.toModel(firstGiftCertificateDto);
        when(certificateRepository.read(anyLong())).thenReturn(Optional.of(certificate));
        OrderDto createdOrder = orderService.create(orderDtoToCreate);
        assertEquals(orderDtoToCreate, createdOrder);
        verify(orderRepository).create(any());
    }


}