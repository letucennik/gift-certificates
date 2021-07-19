package com.epam.esm.repository.impl;

import com.epam.esm.repository.GiftCertificateRepositoryCustom;
import com.epam.esm.repository.entity.GiftCertificate;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.repository.query.QueryBuilder;
import com.epam.esm.repository.query.SortContext;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class GiftCertificateRepositoryCustomImpl implements GiftCertificateRepositoryCustom {

    public static final String NAME = "name";
    public static final String TAG = "tag";
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<GiftCertificate> findByParameters(List<String> tagNames, String partValue, SortContext context, Pageable pageable) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> query = builder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = query.from(GiftCertificate.class);
        query.select(root);
        List<Predicate> predicates = new ArrayList<>();
        if (tagNames != null && !tagNames.isEmpty()) {
            predicates.add(searchByTagNames(builder, root, tagNames));
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
        return entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    private Predicate searchByTagNames(CriteriaBuilder cb, Root<GiftCertificate> root, List<String> tagNames) {
        Predicate result;
        if (tagNames.size() > 1) {
            result = getPredicateLotsOfTags(cb, root, tagNames);
        } else {
            Join<GiftCertificate, Tag> certificateTagJoin = root.join("certificateTags").join(TAG);
            result = cb.equal(certificateTagJoin.get(NAME), tagNames.get(0));
        }
        return result;
    }

    private Predicate getPredicateLotsOfTags(CriteriaBuilder cb, Root<GiftCertificate> root, List<String> tagsName) {
        List<Predicate> predicateList = tagsName.stream()
                .map(name -> joinTags(cb, root, name))
                .collect(Collectors.toList());
        Predicate[] predicates = new Predicate[predicateList.size()];
        return cb.and(predicateList.toArray(predicates));
    }

    private Predicate joinTags(CriteriaBuilder cb, Root<GiftCertificate> root, String name) {
        Join<GiftCertificate, Tag> certificateTagJoin = root.join("certificateTags").join(TAG);
        return cb.equal(certificateTagJoin.get(NAME), name);
    }

    private Predicate buildPredicateByPartInfo(Root<GiftCertificate> root, String partValue, CriteriaBuilder builder) {
        QueryBuilder buildHelper = new QueryBuilder(builder);
        String regexValue = buildHelper.convertToRegex(partValue);
        Predicate predicateByNameInfo = builder.like(root.get(NAME), regexValue);
        Predicate predicateByDescriptionInfo = builder.like(root.get("description"), regexValue);
        return builder.or(predicateByNameInfo, predicateByDescriptionInfo);
    }


}
