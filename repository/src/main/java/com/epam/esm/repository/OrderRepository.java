package com.epam.esm.repository;

import com.epam.esm.entity.Order;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderRepository {

    /**
     * Creates new order.
     *
     * @param order Order to create
     * @return created order
     */
    Order create(Order order);

    /**
     * Gets all Orders by User id
     *
     * @param userId User id to search
     * @param pageable object with pagination info(page number, page size)
     * @return found orders
     */
    List<Order> getUserOrders(long userId, Pageable pageable);

}
