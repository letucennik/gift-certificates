package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.exception.NoSuchEntityException;
import org.springframework.data.domain.Pageable;

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
     * Gets order by id.
     *
     * @param id Order id to search
     * @return found Order
     * @throws NoSuchEntityException when such order doesn't exists
     */
    OrderDto find(long id);

    /**
     * Gets all Orders by User id
     *
     * @param userId User id to search
     * @param pageable object with pagination info(page number, page size)
     * @return found orderDtos
     */
    List<OrderDto> getUserOrders(long userId, Pageable pageable);
}
