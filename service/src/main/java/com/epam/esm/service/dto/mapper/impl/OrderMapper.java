package com.epam.esm.service.dto.mapper.impl;

import com.epam.esm.repository.entity.GiftCertificate;
import com.epam.esm.repository.entity.Order;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class OrderMapper implements Mapper<Order, OrderDto> {

    private final Logger logger = LoggerFactory.getLogger(OrderMapper.class);
    private final ModelMapper mapper;
    private final Mapper<GiftCertificate, GiftCertificateDto> giftCertificateDtoMapper;

    @Autowired
    public OrderMapper(ModelMapper mapper,
                       Mapper<GiftCertificate, GiftCertificateDto> giftCertificateDtoMapper) {
        this.mapper = mapper;
        this.giftCertificateDtoMapper = giftCertificateDtoMapper;
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
