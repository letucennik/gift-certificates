package com.epam.esm.entity;

import com.epam.esm.entity.CertificateTag;
import com.epam.esm.entity.GiftCertificate;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@StaticMetamodel(GiftCertificate.class)
public class GiftCertificate_ {

    public static volatile SingularAttribute<GiftCertificate, Long> id;
    public static volatile SingularAttribute<GiftCertificate, String> name;
    public static volatile SingularAttribute<GiftCertificate,String> description;
    public static volatile SingularAttribute<GiftCertificate, BigDecimal> price;
    public static volatile SingularAttribute<GiftCertificate, LocalDateTime> createDate;
    public static volatile SingularAttribute<GiftCertificate, LocalDateTime> lastUpdateDate;
    public static volatile SingularAttribute<GiftCertificate, Integer> duration;
    public static volatile SetAttribute<GiftCertificate, CertificateTag> certificateTags;
}
