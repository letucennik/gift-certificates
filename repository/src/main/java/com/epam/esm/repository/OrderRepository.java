package com.epam.esm.repository;

import com.epam.esm.entity.Order;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    /**
     * Creates new order.
     *
     * @param order Order to create
     * @return created order
     */
    Order create(Order order);

    Optional<Order> find(long id);

    /**
     * Gets all Orders by User id
     *
     * @param userId User id to search
     * @param pageable object with pagination info(page number, page size)
     * @return found orders
     */
    List<Order> getUserOrders(long userId, Pageable pageable);

}
