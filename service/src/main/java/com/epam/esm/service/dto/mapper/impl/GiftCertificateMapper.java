package com.epam.esm.service.dto.mapper.impl;

import com.epam.esm.repository.entity.CertificateTag;
import com.epam.esm.repository.entity.GiftCertificate;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashSet;
import java.util.Objects;

@Component
public class GiftCertificateMapper implements Mapper<GiftCertificate, GiftCertificateDto> {

    private final ModelMapper mapper;
    private final Mapper<Tag, TagDto> tagMapper;

    @Autowired
    public GiftCertificateMapper(ModelMapper mapper, Mapper<Tag, TagDto> tagMapper) {
        this.mapper = mapper;
        this.tagMapper = tagMapper;
    }

    @Override
    public GiftCertificate toModel(GiftCertificateDto dto) {
        GiftCertificate certificate = Objects.isNull(dto) ? null : mapper.map(dto, GiftCertificate.class);
        certificate.setDuration(Duration.ofDays(dto.getDuration()));
        if (dto.getTags() != null) {
            for (TagDto tagDto : dto.getTags()) {
                CertificateTag certificateTag = new CertificateTag();
                certificateTag.setCertificate(certificate);
                certificateTag.setTag(tagMapper.toModel(tagDto));
                certificate.getCertificateTags().add(certificateTag);
            }
        }
        return certificate;
    }

    @Override
    public GiftCertificateDto toDto(GiftCertificate model) {
        GiftCertificateDto dto = GiftCertificateDto.builder()
                .createDate(model.getCreateDate())
                .description(model.getDescription())
                .lastUpdateDate(model.getLastUpdateDate())
                .price(model.getPrice())
                .name(model.getName())
                .id(model.getId())
                .build();
        dto.setTags(new HashSet<>());
        if (model.getDuration() != null) {
            dto.setDuration(model.getDuration().toDays());
        }
        if (model.getCertificateTags() != null) {
            for (CertificateTag certificateTag : model.getCertificateTags()) {
                dto.getTags().add(tagMapper.toDto(certificateTag.getTag()));
            }
        }
        return dto;
    }
}
