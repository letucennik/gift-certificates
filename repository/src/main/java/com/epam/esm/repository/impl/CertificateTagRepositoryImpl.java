package com.epam.esm.repository.impl;

import com.epam.esm.entity.CertificateTag;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DAOException;
import com.epam.esm.repository.CertificateTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class CertificateTagRepositoryImpl implements CertificateTagRepository {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public CertificateTag create(long certificateId, long tagId) {
        CertificateTag certificateTag = new CertificateTag();
        GiftCertificate certificate = entityManager.find(GiftCertificate.class, certificateId);
        if (certificate == null) {
            throw new DAOException("Trying to create CertificateTag with non-existing certificate");
        }
        certificateTag.setCertificate(certificate);
        Tag tag = entityManager.find(Tag.class, tagId);
        if (tag == null) {
            throw new DAOException("Trying to create CertificateTag with non-existing tag");
        }
        certificateTag.setTag(tag);
        certificate.getCertificateTags().add(certificateTag);
        tag.getCertificateTags().add(certificateTag);
        entityManager.persist(certificateTag);
        return certificateTag;
    }

    @Override
    public List<Long> findTagsIdByCertificateId(long certificateId) {
        return null;
    }


}
