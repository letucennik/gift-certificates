package com.epam.esm.repository;

import java.util.List;

public interface CertificateTagRepository {

    void create(long giftCertificateId, long tagId);

    List<Long> findTagsIdByCertificateId(long certificateId);
}
