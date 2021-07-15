package com.epam.esm.repository.impl;

import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.repository.exception.DAOException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

@Repository
public class TagRepositoryImpl implements TagRepository {

    private static final String GET_MOST_WIDELY_USED_TAG = "SELECT tag_id AS id, tag_name AS name FROM " +
            " (SELECT tag.id AS tag_id, tag.name AS tag_name, COUNT(tag_id) AS tag_count FROM tag " +
            " JOIN m2m_certificates_tags ON tag.id = m2m_certificates_tags.tag_id " +
            " JOIN gift_certificate ON m2m_certificates_tags.gift_certificate_id = gift_certificate.id " +
            " JOIN order_certificates ON gift_certificate.id = order_certificates.certificate_id " +
            " JOIN orders ON order_certificates.order_id = orders.id " +
            " JOIN (SELECT SUM(cost) AS orders_cost, user_id AS ui FROM orders GROUP BY user_id)" +
            " AS a ON orders.user_id = a.ui WHERE orders_cost = " +
            " (SELECT SUM(cost) AS orders_cost FROM orders GROUP BY user_id ORDER BY orders_cost DESC LIMIT 1)" +
            " GROUP BY tag_id ORDER BY tag_count DESC LIMIT 1) AS b";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Tag create(Tag tag) {
        try {
            entityManager.persist(tag);
        } catch (PersistenceException e) {
            throw new DAOException(e);
        }
        return tag;
    }

    @Override
    public Optional<Tag> read(long id) {
        return Optional.ofNullable(entityManager.find(Tag.class, id));
    }

    @Override
    public void delete(long id) {
        Tag tag = entityManager.find(Tag.class, id);
        try {
            entityManager.remove(tag);
        } catch (IllegalArgumentException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public Optional<Tag> findByName(String name) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        criteriaQuery.where(criteriaBuilder.equal(root.get("name"), name));
        return entityManager.createQuery(criteriaQuery).getResultList().stream().findAny();
    }

    @Override
    public Tag getMostWidelyUsedTag() {
        Tag mostWidelyUsedTag;
        try {
            mostWidelyUsedTag = (Tag) entityManager
                    .createNativeQuery(GET_MOST_WIDELY_USED_TAG, Tag.class)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new DAOException(e);
        }
        return mostWidelyUsedTag;
    }


}
