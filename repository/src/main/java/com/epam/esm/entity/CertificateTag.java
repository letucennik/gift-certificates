package com.epam.esm.entity;

import com.epam.esm.entity.audit.AuditListener;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditListener.class)
@Table(name = "m2m_certificates_tags")
public class CertificateTag {

    @EmbeddedId
    private CertificateTagKey id;

    @ManyToOne
    @MapsId("giftCertificateId")
    @JoinColumn(name = "gift_certificate_id")
    private GiftCertificate certificate;

    @ManyToOne
    @MapsId("tagId")
    @JoinColumn(name = "tag_id")
    private Tag tag;
}
