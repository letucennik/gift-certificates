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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
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
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CertificateTag> query = builder.createQuery(CertificateTag.class);
        Root<CertificateTag> root = query.from(CertificateTag.class);
        query.select(root).where(builder.equal(root.get("certificate").get("id"), certificateId));
        List<Long> result = new ArrayList<>();
        List<CertificateTag> certificateTags = entityManager.createQuery(query).getResultList();
        certificateTags.forEach(x -> result.add(x.getTag().getId()));
        return result;
    }


}
