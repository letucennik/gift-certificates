package com.epam.esm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@Table(name = "m2m_certificates_tags")
public class CertificateTagKey implements Serializable {

    @Column(name = "gift_certificate_id")
    private long giftCertificateId;

    @Column(name = "tag_id")
    private long tagId;
}
