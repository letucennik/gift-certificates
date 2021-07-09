package com.epam.esm.repository.impl;

import com.epam.esm.entity.User;
import com.epam.esm.exception.DAOException;
import com.epam.esm.repository.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User create(User user) {
        try {
            entityManager.persist(user);
        } catch (PersistenceException e) {
            throw new DAOException(e);
        }
        return user;
    }

    @Override
    public Optional<User> read(long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public Optional<User> findByName(String name) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        criteriaQuery.where(criteriaBuilder.equal(root.get("name"), name));
        return entityManager.createQuery(criteriaQuery).getResultList().stream().findAny();
    }

    @Override
    public List<User> getAll(Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        criteriaQuery.from(User.class);
        return entityManager.createQuery(criteriaQuery)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }
}
