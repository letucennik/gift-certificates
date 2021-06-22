package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.query.SortContext;

import java.util.List;

public interface GiftCertificateService {

    GiftCertificateDto create(GiftCertificateDto giftCertificateDto);

    GiftCertificate read(long id);

    GiftCertificateDto update(long id, GiftCertificateDto dto);

    List<GiftCertificateDto> findByParameters(String tagName, String partValue, SortContext sortContext);

    void delete(long id);
}
