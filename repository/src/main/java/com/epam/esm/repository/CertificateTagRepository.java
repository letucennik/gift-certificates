package com.epam.esm.repository;

import java.util.List;

public interface CertificateTagRepository {

    /**
     * Creates new certificate-tag reference.
     *
     * @param giftCertificateId certificate id
     * @param tagId             tag id
     */
    void create(long giftCertificateId, long tagId);

    /**
     * Finds tags' ids by certificate id
     *
     * @param certificateId certificate id
     * @return list of tags' ids assigned to corresponding certificate
     */
    List<Long> findTagsIdByCertificateId(long certificateId);
}
