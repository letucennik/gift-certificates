package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.query.SortContext;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateRepository {

    /**
     * Creates new certificate.
     *
     * @param giftCertificate certificate to create
     * @return id of created certificate
     */
    long create(GiftCertificate giftCertificate);

    /**
     * Finds certificate by id.
     *
     * @param id certificate id to find
     * @return Optional of found certificate
     */
    Optional<GiftCertificate> read(long id);

    /**
     * Updates certificate
     *
     * @param certificate certificate to update
     */
    GiftCertificate update(GiftCertificate certificate);

    /**
     * Deletes certificate by id.
     *
     * @param id certificate id to search
     */
    void delete(long id);

    /**
     * Gets all certificates with tags and optional sorting/filtering
     *
     * @param tagName   tag name to filter certificates
     * @param partValue part info of name/desc to filter certificates
     * @param context   columns to sort certificates and order types
     * @param pageable object with pagination info(page number, page size)
     * @return List of sorted/filtered certificates with tags
     */
    List<GiftCertificate> findByParameters(String tagName, String partValue, SortContext context, Pageable pageable);

}
