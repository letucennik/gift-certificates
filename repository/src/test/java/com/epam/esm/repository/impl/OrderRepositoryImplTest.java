package com.epam.esm.repository.impl;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.repository.config.TestJdbcConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestJdbcConfig.class})
@Transactional
class OrderRepositoryImplTest {

    private Order orderToCreate;
    private User user;

    @Autowired
    private OrderRepositoryImpl orderRepository;

    @Autowired
    private UserRepositoryImpl userRepository;

    @BeforeEach
    void init() {
        orderToCreate = new Order();
        user = userRepository.findByName("user 1").get();
        orderToCreate.setUser(user);
        orderToCreate.setDate(LocalDateTime.now());
        orderToCreate.setCost(new BigDecimal("45.00"));
    }


    @Test
    void testShouldCreate() {
        Order newOrder = orderRepository.create(orderToCreate);
        assertNotNull(newOrder);
    }

    @Test
    void testShouldGetUserOrders() {
        Assertions.assertEquals(2, orderRepository.getUserOrders(2, PageRequest.of(0, 25)).size());
    }

    @Test
    void testShouldGetUserOrdersWithPagination() {
        Assertions.assertEquals(1, orderRepository.getUserOrders(2, PageRequest.of(0, 1)).size());
    }
}