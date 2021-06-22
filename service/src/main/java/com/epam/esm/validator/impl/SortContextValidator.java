package com.epam.esm.validator.impl;

import com.epam.esm.repository.query.SortContext;
import com.epam.esm.validator.Validator;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class SortContextValidator implements Validator<SortContext> {

    private static final List<String> CERTIFICATE_FIELD_NAMES = Arrays.asList("id", "name", "description", "price", "duration", "createDate", "lastUpdateDate");

    @Override
    public boolean isValid(SortContext item) {
        return CERTIFICATE_FIELD_NAMES.containsAll(item.getSortColumns())
                && (item.getOrderTypes().contains(SortContext.OrderType.ASC)
                || item.getOrderTypes().contains(SortContext.OrderType.DESC));
    }
}
