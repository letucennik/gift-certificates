package com.epam.esm.repository.query;

import com.epam.esm.repository.entity.GiftCertificate;
import com.epam.esm.repository.entity.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class QueryBuilder {

    private static final String ANY_REGEX = "%";

    private final CriteriaBuilder builder;

    public QueryBuilder(CriteriaBuilder criteriaBuilder) {
        this.builder = criteriaBuilder;
    }

    public <T> List<Order> buildOrderList(Root<T> root, SortContext sortParameters) {
        List<Order> orderList = new ArrayList<>();
        for (int i = 0; i < sortParameters.getSortColumns().size(); i++) {
            String column = sortParameters.getSortColumns().get(i);
            String orderType;
            if (sortParameters.getOrderTypes().size() > i) {
                orderType = sortParameters.getOrderTypes().get(i).toString();
            } else {
                orderType = "ASC";
            }
            Order order;
            if (orderType.equalsIgnoreCase("ASC")) {
                order = builder.asc(root.get(column));
            } else {
                order = builder.desc(root.get(column));
            }
            orderList.add(order);
        }
        return orderList;
    }

    public String convertToRegex(String value) {
        return ANY_REGEX + value + ANY_REGEX;
    }


}