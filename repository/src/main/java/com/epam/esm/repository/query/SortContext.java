package com.epam.esm.repository.query;

import java.util.List;

public class SortContext {

    private final List<String> sortColumns;
    private final List<OrderType> orderTypes;

    public enum OrderType {
        ASC, DESC
    }

    public SortContext(List<String> sortColumns, List<OrderType> orderTypes) {
        this.sortColumns = sortColumns;
        this.orderTypes = orderTypes;
    }

    public List<String> getSortColumns() {
        return sortColumns;
    }

    public List<OrderType> getOrderTypes() {
        return orderTypes;
    }
}
