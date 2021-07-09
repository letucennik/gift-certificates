package com.epam.esm.repository.impl;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.repository.config.TestJdbcConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestJdbcConfig.class})
@Transactional
class OrderRepositoryImplTest {

    private Order orderToCreate;

    @Autowired
    private OrderRepositoryImpl orderRepository;

    @BeforeEach
    void init(){
       orderToCreate=new Order();
       orderToCreate.setUser(new User(1L,"user 1"));
       orderToCreate.setDate(LocalDateTime.now());
       orderToCreate.setCost(new BigDecimal("45.00"));
    }


    @Test
    void testShouldCreate() {
        Order newOrder=orderRepository.create(orderToCreate);
        assertNotNull(newOrder);
    }

    @Test
    void getUserOrders() {
    }
}