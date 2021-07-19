package com.epam.esm.repository;

import com.epam.esm.repository.entity.GiftCertificate;
import com.epam.esm.repository.query.SortContext;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GiftCertificateRepositoryCustom {
    /**
     * Gets all certificates with tags and optional sorting/filtering
     *
     * @param tagNames  Tag names to filter
     * @param partValue part info of name/desc to filter certificates
     * @param context   columns to sort certificates and order types
     * @param pageable  object with pagination info(page number, page size)
     * @return List of sorted/filtered certificates with tags
     */

    List<GiftCertificate> findByParameters(List<String> tagNames, String partValue, SortContext context, Pageable pageable);
}
