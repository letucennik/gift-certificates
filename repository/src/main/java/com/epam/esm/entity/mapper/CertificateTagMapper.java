package com.epam.esm.entity.mapper;

import com.epam.esm.entity.CertificateTag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;


@Service
public class CertificateTagMapper implements RowMapper<CertificateTag> {

    private static final String CERTIFICATE_ID = "gift_certificate_id";
    private static final String TAG_ID = "tag_id";

    @Override
    public CertificateTag mapRow(ResultSet resultSet, int i) throws SQLException {
        return new CertificateTag(resultSet.getLong(CERTIFICATE_ID), resultSet.getLong(TAG_ID));
    }
}
