package com.epam.esm.repository.query;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public final class QueryBuilder {

    private static final String ANY_REGEX = "%";

    private final CriteriaBuilder builder;

    public QueryBuilder(CriteriaBuilder criteriaBuilder) {
        this.builder = criteriaBuilder;
    }

    public <T> Predicate buildOrEqualPredicates(Path<T> root, String columnName, List<?> values) {
        int counter = 0;
        Predicate predicate = null;
        for (Object value : values) {
            Predicate currentPredicate = builder.equal(root.get(columnName), value);
            if (counter++ == 0) {
                predicate = currentPredicate;
            } else {
                predicate = builder.or(predicate, currentPredicate);
            }
        }

        return predicate;
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