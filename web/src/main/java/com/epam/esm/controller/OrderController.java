package com.epam.esm.controller;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.util.LinkAdder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/v1/users/{userId}/orders")
public class OrderController {
    private final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;
    private final UserService userService;
    private final LinkAdder<OrderDto> orderDtoLinkAdder;

    @Autowired
    public OrderController(OrderService orderService,
                           LinkAdder<OrderDto> orderDtoLinkAdder,
                           UserService userService) {
        this.orderService = orderService;
        this.orderDtoLinkAdder = orderDtoLinkAdder;
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto createOrder(@PathVariable long userId,
                                @RequestBody OrderDto dto) {
        dto.setUser(userService.read(userId));
        OrderDto createdOrderDto = orderService.create(dto);
        orderDtoLinkAdder.addLinks(createdOrderDto);
        return createdOrderDto;
    }
}
