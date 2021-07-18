package com.epam.esm.service.dto;

import com.epam.esm.repository.entity.GiftCertificate;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiftCertificateDto extends RepresentationModel<GiftCertificateDto> {

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm'Z'";

    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    @JsonFormat(pattern = DATE_FORMAT)
    private LocalDateTime createDate;
    @JsonFormat(pattern = DATE_FORMAT)
    private LocalDateTime lastUpdateDate;
    private int duration;
    private Set<TagDto> tags = new HashSet<>();

    public GiftCertificateDto(GiftCertificate certificate) {
        this.id = certificate.getId();
        this.name = certificate.getName();
        this.description = certificate.getDescription();
        this.price = certificate.getPrice();
        this.createDate = certificate.getCreateDate();
        this.lastUpdateDate = certificate.getLastUpdateDate();
        this.duration = certificate.getDuration();
    }

}
