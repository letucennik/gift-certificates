package com.epam.esm.repository.impl;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.DAOException;
import com.epam.esm.repository.OrderRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Order create(Order order) {
        try {
            entityManager.persist(order);
        } catch (PersistenceException e) {
            throw new DAOException(e);
        }
        return order;
    }

    @Override
    public Optional<Order> read(long id) {
        return Optional.ofNullable(entityManager.find(Order.class, id));
    }

    @Override
    public List<Order> getUserOrders(long userId, Pageable pageable) {
        return entityManager.createQuery(buildCriteriaQuery(userId))
                .setMaxResults(pageable.getPageSize())
                .setFirstResult((int) pageable.getOffset())
                .getResultList();
    }

    private CriteriaQuery<Order> buildCriteriaQuery(long userId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);
        Join<User, Order> userJoin = root.join("user");
        Predicate joinIdPredicate = criteriaBuilder.equal(userJoin.get("id"), userId);
        criteriaQuery.where(joinIdPredicate);
        return criteriaQuery;
    }
}
