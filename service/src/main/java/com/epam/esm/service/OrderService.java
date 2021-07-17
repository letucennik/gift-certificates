package com.epam.esm.service;

import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.exception.NoSuchEntityException;

import java.util.List;

public interface OrderService {

    /**
     * Creates new order.
     *
     * @param orderDto OrderDto to create
     * @return created orderDto
     */
    OrderDto create(OrderDto orderDto);

    /**
     * Gets order by user id and its id.
     *
     * @param userId User id to search
     * @param orderId Order id to search
     * @return found Order
     * @throws NoSuchEntityException when such order doesn't exists
     */
    OrderDto findByUserId(long userId, long orderId);

    /**
     * Gets all Orders by User id
     *
     * @param userId User id to search
     * @param page   page number of orders
     * @param size   page size
     * @return found orderDtos
     */
    List<OrderDto> getUserOrders(long userId, int page, int size);
}
