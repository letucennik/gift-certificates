package com.epam.esm.repository.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificate_;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.Tag_;
import com.epam.esm.exception.DAOException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.query.QueryBuilder;
import com.epam.esm.repository.query.SortContext;
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
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public long create(GiftCertificate giftCertificate) {
        try {
            entityManager.persist(giftCertificate);
        } catch (PersistenceException e) {
            throw new DAOException(e);
        }
        return giftCertificate.getId();
    }

    @Override
    public Optional<GiftCertificate> read(long id) {
        return Optional.ofNullable(entityManager.find(GiftCertificate.class, id));
    }

    @Override
    public GiftCertificate update(GiftCertificate certificate) {
        return entityManager.merge(certificate);
    }

    @Override
    public void delete(long id) {
        GiftCertificate certificate = entityManager.find(GiftCertificate.class, id);
        try {
            entityManager.remove(certificate);
        } catch (IllegalArgumentException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public List<GiftCertificate> findByParameters(List<String> tagNames, String partValue, SortContext context, Pageable pageable) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> query = builder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = query.from(GiftCertificate.class);
        query.select(root);
        List<Predicate> predicates = new ArrayList<>();
        if (tagNames != null && !tagNames.isEmpty()) {
            predicates.add(buildPredicateByTagName(root, tagNames, builder));
        }
        if (partValue != null) {
            predicates.add(buildPredicateByPartInfo(root, partValue, builder));
        }
        if (context != null && context.getSortColumns() != null) {
            QueryBuilder buildHelper = new QueryBuilder(builder);
            List<Order> orderList = buildHelper.buildOrderList(root, context);
            if (!orderList.isEmpty()) {
                query.orderBy(orderList);
            }
        }
        query.where(predicates.toArray(new Predicate[0])).distinct(true);
        return entityManager.createQuery(query).setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize()).getResultList();
    }

    private Predicate buildPredicateByTagName(Root<GiftCertificate> root, List<String> tagNames, CriteriaBuilder builder) {
        Join<GiftCertificate, Tag> certificateTagJoin = root.join(GiftCertificate_.certificateTags).join("tag");
        QueryBuilder buildHelper = new QueryBuilder(builder);
        return buildHelper.buildOrEqualPredicates(certificateTagJoin,"name", tagNames);
    }

    private Predicate buildPredicateByPartInfo(Root<GiftCertificate> root, String partValue, CriteriaBuilder builder) {
        QueryBuilder buildHelper = new QueryBuilder(builder);
        String regexValue = buildHelper.convertToRegex(partValue);
        Predicate predicateByNameInfo = builder.like(root.get(GiftCertificate_.name), regexValue);
        Predicate predicateByDescriptionInfo = builder.like(root.get(GiftCertificate_.description), regexValue);
        return builder.or(predicateByNameInfo, predicateByDescriptionInfo);
    }


}
