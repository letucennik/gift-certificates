package com.epam.esm.dto.mapper.impl;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.mapper.Mapper;
import com.epam.esm.entity.Order;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class OrderMapper implements Mapper<Order, OrderDto> {

    private final ModelMapper mapper;

    @Autowired
    public OrderMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Order toModel(OrderDto dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, Order.class);
    }

    @Override
    public OrderDto toDto(Order model) {
        return Objects.isNull(model) ? null : mapper.map(model, OrderDto.class);
    }

}
