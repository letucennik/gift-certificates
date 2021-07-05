package com.epam.esm.dto.mapper;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.CertificateTag;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.CertificateTagRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class GiftCertificateMapper {

    private final ModelMapper mapper;
    private final CertificateTagRepository certificateTagRepository;

    @Autowired
    public GiftCertificateMapper(ModelMapper mapper, CertificateTagRepository certificateTagRepository) {
        this.mapper = mapper;
        this.certificateTagRepository = certificateTagRepository;
    }

    public GiftCertificate toModel(GiftCertificateDto dto) {
        GiftCertificate certificate = Objects.isNull(dto) ? null : mapper.map(dto, GiftCertificate.class);
        TagMapper mapper = new TagMapper(new ModelMapper());
        for (TagDto tagDto : dto.getTags()) {
            CertificateTag certificateTag = new CertificateTag();
            certificateTag.setCertificate(certificate);
            certificateTag.setTag(mapper.toModel(tagDto));
            certificate.getCertificateTags().add(certificateTag);
        }
        return certificate;
    }

    public GiftCertificateDto toDTO(GiftCertificate model) {
        GiftCertificateDto dto = Objects.isNull(model) ? null : mapper.map(model, GiftCertificateDto.class);
        TagMapper mapper = new TagMapper(new ModelMapper());
        for(CertificateTag certificateTag:model.getCertificateTags()){
            dto.getTags().add(mapper.toDTO(certificateTag.getTag()));
        }
        return dto;
    }
}
