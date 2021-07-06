package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.exception.DuplicateEntityException;
import com.epam.esm.exception.InvalidParameterException;
import com.epam.esm.exception.InvalidSortParameterException;
import com.epam.esm.exception.NoSuchEntityException;
import com.epam.esm.repository.query.SortContext;

import java.util.List;

public interface GiftCertificateService {

    /**
     * Creates new gift certificate with optional tags.
     *
     * @param giftCertificateDto certificateDto to create Certificate/Tags
     * @return GiftCertificateDto created dto
     * @throws InvalidParameterException when certificate or tags parameters are invalid
     * @throws DuplicateEntityException        when several identical tags are passed
     */
    GiftCertificateDto create(GiftCertificateDto giftCertificateDto);

    /**
     * Gets gift certificate by id.
     *
     * @param id certificate id to search
     * @return found gift certificate
     * @throws NoSuchEntityException when such certificate doesn't exist
     */
    GiftCertificateDto read(long id);

    /**
     * Updates Certificate by id
     * with updating only fields that are passed
     *
     * @param id  certificate id to search
     * @param dto update information
     * @return updated GiftCertificateDto
     * @throws NoSuchEntityException           when such certificate doesn't exist
     * @throws InvalidParameterException when update info is invalid
     */
    GiftCertificateDto update(long id, GiftCertificateDto dto);

    /**
     * Gets all certificates with tags and optional sorting/filtering
     *
     * @param tagName     tag name to filter certificates
     * @param partValue   part info of name/desc to filter certificates
     * @param sortContext columns to sort certificates and order types
     * @param page        page number of Certificates
     * @param size        page size
     * @return List of sorted/filtered certificates with tags
     * @throws InvalidSortParameterException when sort parameters are invalid
     */
    List<GiftCertificateDto> findByParameters(String tagName, String partValue, SortContext sortContext, int page, int size);

    /**
     * Deletes certificate by id.
     *
     * @param id certificate id to search
     * @throws NoSuchEntityException when certificate is not found
     */
    void delete(long id);
}
