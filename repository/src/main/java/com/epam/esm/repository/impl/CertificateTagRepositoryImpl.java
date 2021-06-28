package com.epam.esm.repository.impl;

import com.epam.esm.repository.CertificateTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class CertificateTagRepositoryImpl implements CertificateTagRepository {

    private static final String CREATE_CERTIFICATE_TAG_REFERENCE = "INSERT INTO m2m_certificates_tags (gift_certificate_id, tag_id) VALUES (?, ?)";
    private static final String FIND_TAGS_ID_BY_CERTIFICATE_ID = "SELECT tag_id FROM m2m_certificates_tags WHERE gift_certificate_id=?";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void create(long giftCertificateId, long tagId) {
        jdbcTemplate.update(CREATE_CERTIFICATE_TAG_REFERENCE, giftCertificateId, tagId);
    }

    @Override
    public List<Long> findTagsIdByCertificateId(long certificateId) {
        return jdbcTemplate.query(FIND_TAGS_ID_BY_CERTIFICATE_ID, (resultSet, i) -> resultSet.getLong("tag_id"), certificateId);
    }


}
