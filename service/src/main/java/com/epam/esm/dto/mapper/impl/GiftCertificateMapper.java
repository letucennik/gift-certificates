package com.epam.esm.dto.mapper.impl;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.mapper.Mapper;
import com.epam.esm.entity.CertificateTag;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        GiftCertificateDto dto = Objects.isNull(model) ? null : mapper.map(model, GiftCertificateDto.class);
        TagMapper mapper = new TagMapper(new ModelMapper());
        if (model.getCertificateTags() != null) {
            for (CertificateTag certificateTag : model.getCertificateTags()) {
                dto.getTags().add(mapper.toDto(certificateTag.getTag()));
            }
        }
        return dto;
    }
}
