package com.epam.esm.validator.impl;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.validator.Validator;

public class OrderValidator implements Validator<OrderDto> {
    @Override
    public boolean isValid(OrderDto item) {
        return false;
    }
}
