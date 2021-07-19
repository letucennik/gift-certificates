package com.epam.esm.repository;

import com.epam.esm.repository.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {

    /**
     * Finds order by id and userId.
     *
     * @param id     order id to find
     * @param userId user id
     * @return Optional of found order
     */
    Optional<Order> findByUserIdAndId(long userId, long id);

    /**
     * Gets all Orders by User id
     *
     * @param userId   User id to search
     * @param pageable object with pagination info(page number, page size)
     * @return found orders
     */
    List<Order> findAllByUserId(long userId, Pageable pageable);

}
