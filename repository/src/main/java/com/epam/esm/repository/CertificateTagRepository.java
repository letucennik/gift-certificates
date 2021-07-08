package com.epam.esm.repository;

import com.epam.esm.entity.CertificateTag;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;

import java.util.List;

public interface CertificateTagRepository {

    /**
     * Creates new certificate-tag reference.
     *
     * @param certificateId certificate id
     * @param tagId        tag id
     */
    CertificateTag create(long certificateId, long tagId);

    /**
     * Finds tags' ids by certificate id
     *
     * @param certificateId certificate id
     * @return list of tags assigned to corresponding certificate
     */
    List<Tag> findTagsIdByCertificateId(long certificateId);
}
