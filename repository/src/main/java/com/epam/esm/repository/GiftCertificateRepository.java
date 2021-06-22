package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.query.SortContext;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GiftCertificateRepository {

    long create(GiftCertificate giftCertificate);

    Optional<GiftCertificate> read(long id);

    void update(long id, Map<String, Object> giftCertificateUpdateInfo);

    int delete(long id);

    List<GiftCertificate> findByParameters(String tagName, String partValue, SortContext context);

}
