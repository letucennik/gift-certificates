package com.epam.esm.repository.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.query.QueryBuilder;
import com.epam.esm.repository.query.SortContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.*;

@Repository
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {
    private static final String INSERT_CERTIFICATE = "INSERT INTO gift_certificate (name, description, price, duration, create_date, last_update_date) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_GIFT_CERTIFICATES_BY_ID = "SELECT id, name, description, price, duration, create_date, last_update_date from gift_certificate where id=?";
    private static final String DELETE_GIFT_CERTIFICATE = "DELETE FROM gift_certificate WHERE id = ?";
    private static final String SELECT_ALL = "SELECT id, name, description, price, duration, create_date, last_update_date from gift_certificate";
    private static final String UPDATE_GIFT_CERTIFICATE = "UPDATE gift_certificate SET last_update_date=NOW(), ";
    private static final String SELECT_CERTIFICATES_BY_TAG_NAME = "SELECT gc.id, gc.name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date FROM" +
            " gift_certificate gc JOIN m2m_certificates_tags ON gc.id = m2m_certificates_tags.gift_certificate_id" +
            " JOIN tag ON tag.id=m2m_certificates_tags.tag_id WHERE tag.name=%s";
    private static final String SELECT_ALL_CERTIFICATES_BY_NAME_OR_DESCRIPTION = "SELECT gc.id, gc.name, gc.description," +
            " gc.price, gc.duration, gc.create_date, gc.last_update_date from gift_certificate AS gc\n" +
            " where gc.name like %s OR gc.description like %s";
    private static final String SELECT_BY_PARAMETERS = "SELECT DISTINCT gc.id, gc.name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date FROM" +
            " gift_certificate gc LEFT JOIN m2m_certificates_tags ON gc.id = m2m_certificates_tags.gift_certificate_id LEFT JOIN tag ON tag.id=m2m_certificates_tags.tag_id " +
            "WHERE (%s IS NULL OR tag.name=%s) AND (%s IS NULL OR (gc.name like %s OR gc.description like %s)) ";
    private static final String QUOTES = "'";
    private static final String ANY_CHARACTERS_BEFORE = "'%";
    private static final String ANY_CHARACTERS_AFTER = "%'";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<GiftCertificate> giftCertificateMapper;

    @Autowired
    public GiftCertificateRepositoryImpl(JdbcTemplate jdbcTemplate, RowMapper<GiftCertificate> giftCertificateMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.giftCertificateMapper = giftCertificateMapper;
    }

    @Override
    public long create(GiftCertificate giftCertificate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CERTIFICATE, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, giftCertificate.getName());
            preparedStatement.setString(2, giftCertificate.getDescription());
            preparedStatement.setBigDecimal(3, giftCertificate.getPrice());
            preparedStatement.setInt(4, giftCertificate.getDuration());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(giftCertificate.getCreateDate()));
            preparedStatement.setTimestamp(6, Timestamp.valueOf(giftCertificate.getLastUpdateDate()));
            return preparedStatement;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public Optional<GiftCertificate> read(long id) {
        return jdbcTemplate.query(SELECT_GIFT_CERTIFICATES_BY_ID, giftCertificateMapper, id).stream().findAny();
    }

    @Override
    public void update(long id, Map<String, Object> giftCertificateUpdateInfo) {
        if (!giftCertificateUpdateInfo.isEmpty()) {
            StringBuilder updateQueryBuilder = new StringBuilder();
            updateQueryBuilder.append(UPDATE_GIFT_CERTIFICATE);
            String updateColumnsQuery = QueryBuilder.buildUpdateColumnsQuery(giftCertificateUpdateInfo.keySet());
            updateQueryBuilder.append(updateColumnsQuery);
            updateQueryBuilder.append(" WHERE id=?");
            List<Object> values = new ArrayList<>(giftCertificateUpdateInfo.values());
            values.add(id);
            jdbcTemplate.update(updateQueryBuilder.toString(), values.toArray());
        }
    }

    @Override
    public int delete(long id) {
        return jdbcTemplate.update(DELETE_GIFT_CERTIFICATE, id);
    }

    @Override
    public List<GiftCertificate> findByParameters(String tagName, String partValue, SortContext context) {
        if (tagName != null) {
            tagName = QUOTES + tagName + QUOTES;
        }
        if (partValue != null) {
            partValue = ANY_CHARACTERS_BEFORE + partValue + ANY_CHARACTERS_AFTER;
        }
        String query = String.format(SELECT_BY_PARAMETERS, tagName, tagName, partValue, partValue, partValue);
        if (context != null && context.getSortColumns() != null && !context.getSortColumns().isEmpty()) {
            query += QueryBuilder.buildSortingQuery(context);
        }
        return jdbcTemplate.query(query, giftCertificateMapper);
    }


}
