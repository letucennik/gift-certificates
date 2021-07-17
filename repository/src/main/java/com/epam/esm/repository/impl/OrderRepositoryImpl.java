package com.epam.esm.repository.impl;

import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.entity.Order;
import com.epam.esm.repository.entity.User;
import com.epam.esm.repository.exception.DAOException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
    public Optional<Order> findByUserId(long userId, long id) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteria = builder.createQuery(Order.class);
        Root<Order> root = criteria.from(Order.class);
        Predicate userPredicate = builder.equal(root.get("user").get("id"), userId);
        Predicate orderPredicate = builder.equal(root.get("id"), id);
        criteria.where(userPredicate, orderPredicate);
        return entityManager.createQuery(criteria).getResultStream().findAny();
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
        Predicate joinIdPredicate = joinIdPredicate(criteriaBuilder,criteriaQuery, userId);
        criteriaQuery.where(joinIdPredicate);
        return criteriaQuery;
    }

    private Predicate joinIdPredicate(CriteriaBuilder criteriaBuilder,CriteriaQuery<Order>criteriaQuery, long userId) {
        Root<Order> root = criteriaQuery.from(Order.class);
        return criteriaBuilder.equal(root.get("user").get("id"), userId);
    }
}
