package com.epam.esm.dto.mapper;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class GiftCertificateMapper {

    private final ModelMapper mapper;

    @Autowired
    public GiftCertificateMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public GiftCertificate toModel(GiftCertificateDto dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, GiftCertificate.class);
    }

    public GiftCertificateDto toDTO(GiftCertificate model) {
        return Objects.isNull(model) ? null : mapper.map(model, GiftCertificateDto.class);
    }
}
