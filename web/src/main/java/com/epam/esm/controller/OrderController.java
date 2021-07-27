package com.epam.esm.controller;

import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.controller.util.LinkAdder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/v1/users/{userId}/orders")
public class OrderController {

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
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') and #userId == authentication.principal.id")
    public OrderDto createOrder(@PathVariable long userId,
                                @RequestBody OrderDto dto) {
        dto.setUser(userService.read(userId));
        OrderDto createdOrderDto = orderService.create(dto);
        orderDtoLinkAdder.addLinks(createdOrderDto);
        return createdOrderDto;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('USER') and #userId == authentication.principal.id)")
    public List<OrderDto> getUserOrders(@PathVariable long userId,
                                        @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                        @RequestParam(value = "size", defaultValue = "4", required = false) int size) {
        List<OrderDto> orders = orderService.getUserOrders(userId, page, size);
        return orders.stream()
                .peek(orderDtoLinkAdder::addLinks)
                .collect(Collectors.toList());
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('USER') and #userId == authentication.principal.id)")
    public OrderDto getOrder(@PathVariable long userId, @PathVariable long orderId) {
        OrderDto orderDto = orderService.findByUserId(userId, orderId);
        orderDtoLinkAdder.addLinks(orderDto);
        return orderDto;
    }
}
