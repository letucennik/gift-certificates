package com.epam.esm.repository.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class QueryBuilder {

    private static final Pattern UPPER_CASE_SYMBOL_PATTERN = Pattern.compile("[A-Z]");

    private QueryBuilder() {
    }

    public static String buildUpdateColumnsQuery(Set<String> columns) {
        StringBuilder queryBuilder = new StringBuilder();
        boolean isFirstElement = true;
        for (String column : columns) {
            if (!isFirstElement) {
                queryBuilder.append(", ");
            } else {
                isFirstElement = false;
            }
            queryBuilder.append(column);
            queryBuilder.append("=?");
        }
        return queryBuilder.toString();
    }

    public static String buildSortingQuery(SortContext sortParameters) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("ORDER BY ");
        List<String> sortColumns = convertToDBFields(sortParameters.getSortColumns());
        List<String> orderTypes = sortParameters.getOrderTypes();
        for (int i = 0; i < sortColumns.size(); i++) {
            if (i != 0) {
                queryBuilder.append(", ");
            }
            queryBuilder.append(sortColumns.get(i)).append(" ");
            queryBuilder.append(i < orderTypes.size() ? orderTypes.get(i) : "ASC");
        }
        return queryBuilder.toString();
    }


    private static List<String> convertToDBFields(List<String> javaFields) {
        List<String> DBFields = new ArrayList<>();
        javaFields.forEach(fieldName -> {
            Matcher matcher = UPPER_CASE_SYMBOL_PATTERN.matcher(fieldName);
            while (matcher.find()) {
                String matchedSymbol = matcher.group();
                fieldName = fieldName.replaceAll(matchedSymbol, "_" + matchedSymbol.toLowerCase());
            }
            DBFields.add(fieldName);
        });
        return DBFields;
    }
}