package com.epam.esm.service.validator.impl;

import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.validator.Validator;

public class OrderValidator implements Validator<OrderDto> {
    @Override
    public boolean isValid(OrderDto item) {
        return false;
    }
}
