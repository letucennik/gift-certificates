package com.epam.esm.repository.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DAOException;
import com.epam.esm.repository.TagRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

@Repository
public class TagRepositoryImpl implements TagRepository {

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
    @SuppressWarnings("unchecked")
    public Tag getMostWildlyUsedTag(long userId) {
        return (Tag) entityManager.createNativeQuery(
                "SELECT tag.id AS tag_id, tag.name AS tag_name " +
                        "FROM tag " +
                        "JOIN m2m_certificates_tags gct ON gct.tag_id = tag.id " +
                        "JOIN order_certificates oc ON oc.certificate_id = gct.gift_certificate_id " +
                        "JOIN orders o ON o.id=oc.order_id "+
                        "WHERE o.user_id = :userId " +
                        "GROUP BY tag.id " +
                        "ORDER BY COUNT(tag.id) DESC " +
                        "LIMIT 1")
                .setParameter("userId", userId)
                .getSingleResult();
    }


}
