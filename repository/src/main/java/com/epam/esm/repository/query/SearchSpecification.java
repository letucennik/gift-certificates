package com.epam.esm.repository.query;

import com.epam.esm.repository.entity.GiftCertificate;
import com.epam.esm.repository.entity.Tag;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SearchSpecification {
    public static String NAME = "name";
    public static String TAG = "tag";

    private List<String> tagNames;
    private String partValue;
    private SortContext context;

    public SearchSpecification() {
    }

    public SearchSpecification(List<String> tagNames, String partValue, SortContext context) {
        this.tagNames = tagNames;
        this.partValue = partValue;
        this.context = context;
    }

    public Specification<GiftCertificate> findByParameters() {
        return new Specification<GiftCertificate>() {
            @Override
            public Predicate toPredicate(Root<GiftCertificate> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (tagNames != null && !tagNames.isEmpty()) {
                    predicates.add(searchByTagNames(criteriaBuilder, root, tagNames));
                }
                if (partValue != null) {
                    predicates.add(buildPredicateByPartInfo(root, partValue, criteriaBuilder));
                }
                if (context != null && context.getSortColumns() != null) {
                    QueryBuilder buildHelper = new QueryBuilder(criteriaBuilder);
                    List<Order> orderList = buildHelper.buildOrderList(root, context);
                    if (!orderList.isEmpty()) {
                        criteriaQuery.orderBy(orderList);
                    }
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
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
        };
    }
}
