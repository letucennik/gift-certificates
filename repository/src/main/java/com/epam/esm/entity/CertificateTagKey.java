package com.epam.esm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@Table(name = "m2m_certificates_tags")
public class CertificateTagKey implements Serializable {

    private Long giftCertificateId;

    private Long tagId;
}
