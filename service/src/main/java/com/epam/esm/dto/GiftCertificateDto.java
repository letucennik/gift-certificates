package com.epam.esm.dto;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiftCertificateDto {

    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String createDate;
    private String lastUpdateDate;
    private int duration;
    private List<Tag> tags;

    public GiftCertificateDto(GiftCertificate certificate) {
        this.id = certificate.getId();
        this.name = certificate.getName();
        this.description = certificate.getDescription();
        this.price = certificate.getPrice();
        this.createDate = certificate.getCreateDate().toString();
        this.lastUpdateDate = certificate.getLastUpdateDate().toString();
        this.duration = certificate.getDuration();
        this.tags = new ArrayList<>();
    }

    public GiftCertificateDto(GiftCertificate certificate, List<Tag> tags) {
        this(certificate);
        this.tags = tags;
    }

    public void addTag(Tag tag) {
        this.tags.add(tag);
    }
}
