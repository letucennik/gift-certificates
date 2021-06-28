package com.epam.esm.entity.mapper;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;


@Service
public class GiftCertificateMapper implements RowMapper<GiftCertificate> {

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String PRICE = "price";
    private static final String DURATION = "duration";
    private static final String CREATE_DATE = "create_date";
    private static final String LAST_UPDATE_DATE = "last_update_date";

    @Override
    public GiftCertificate mapRow(ResultSet resultSet, int i) throws SQLException {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(resultSet.getLong(ID));
        giftCertificate.setName(resultSet.getString(NAME));
        giftCertificate.setDescription(resultSet.getString(DESCRIPTION));
        giftCertificate.setPrice(resultSet.getBigDecimal(PRICE));
        giftCertificate.setDuration(resultSet.getInt(DURATION));
        giftCertificate.setCreateDate(resultSet.getTimestamp(CREATE_DATE).toLocalDateTime());
        giftCertificate.setLastUpdateDate(resultSet.getTimestamp(LAST_UPDATE_DATE).toLocalDateTime());
        return giftCertificate;
    }
}
