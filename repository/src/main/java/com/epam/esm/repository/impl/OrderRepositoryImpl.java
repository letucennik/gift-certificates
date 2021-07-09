package com.epam.esm.repository.impl;

import com.epam.esm.entity.Order;
import com.epam.esm.exception.DAOException;
import com.epam.esm.repository.OrderRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

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
        List<Predicate> predicateList = new ArrayList<>();
        predicateList.add(criteriaBuilder.equal(root.get("user_id"), userId));
        criteriaQuery.where(predicateList.toArray(new Predicate[0]));
        return criteriaQuery;
    }
}
