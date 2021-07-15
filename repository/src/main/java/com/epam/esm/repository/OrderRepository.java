package com.epam.esm.repository;

import com.epam.esm.repository.entity.Order;
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

    /**
     * Finds order by id.
     *
     * @param id order id to find
     * @return Optional of found order
     */
    Optional<Order> read(long id);

    /**
     * Finds order by id and userId.
     *
     * @param id     order id to find
     * @param userId user id
     * @return Optional of found order
     */
    Optional<Order> findByUserId(long userId, long id);

    /**
     * Gets all Orders by User id
     *
     * @param userId   User id to search
     * @param pageable object with pagination info(page number, page size)
     * @return found orders
     */
    List<Order> getUserOrders(long userId, Pageable pageable);

}
