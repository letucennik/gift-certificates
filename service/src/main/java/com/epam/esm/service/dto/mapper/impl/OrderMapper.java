package com.epam.esm.service.dto.mapper.impl;

import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.mapper.Mapper;
import com.epam.esm.repository.entity.GiftCertificate;
import com.epam.esm.repository.entity.Order;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class OrderMapper implements Mapper<Order, OrderDto> {

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
        Order order = Objects.isNull(dto) ? null : mapper.map(dto, Order.class);
        order.getCertificates().clear();
        for (GiftCertificateDto certificate : dto.getCertificates()) {
            order.getCertificates().add(giftCertificateDtoMapper.toModel(certificate));
        }
        return order;
    }

    @Override
    public OrderDto toDto(Order model) {
        OrderDto dto = Objects.isNull(model) ? null : mapper.map(model, OrderDto.class);
        dto.getCertificates().clear();
        for (GiftCertificate certificate : model.getCertificates()) {
            dto.getCertificates().add(giftCertificateDtoMapper.toDto(certificate));
        }
        return dto;
    }

}
