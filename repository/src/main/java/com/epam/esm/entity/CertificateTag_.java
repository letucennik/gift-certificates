package com.epam.esm.entity;

import com.epam.esm.entity.CertificateTag;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(CertificateTag.class)
public class CertificateTag_ {

    public static volatile SingularAttribute<CertificateTag, GiftCertificate> certificate;
    public static volatile SingularAttribute<CertificateTag, Tag> tag;
}
