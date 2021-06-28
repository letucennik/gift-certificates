package com.epam.esm.repository.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Objects;
import java.util.Optional;

@Repository
public class TagRepositoryImpl implements TagRepository {

    private static final String CREATE_TAG = "INSERT INTO tag (name) VALUES (?)";
    private static final String SELECT_TAG_BY_ID = "SELECT id, name FROM tag WHERE id=?";
    private static final String SELECT_TAG_BY_NAME = "SELECT id, name FROM tag WHERE name=?";
    private static final String DELETE_TAG = "DELETE FROM tag WHERE id=?";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Tag> tagMapper;

    @Autowired
    public TagRepositoryImpl(JdbcTemplate jdbcTemplate, RowMapper<Tag> tagMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.tagMapper = tagMapper;
    }

    @Override
    public long create(Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(CREATE_TAG, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, tag.getName());
            return preparedStatement;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public Optional<Tag> read(long id) {
        return jdbcTemplate.query(SELECT_TAG_BY_ID, tagMapper, id).stream().findAny();
    }

    @Override
    public int delete(long id) {
        return jdbcTemplate.update(DELETE_TAG, id);
    }

    @Override
    public Optional<Tag> findByName(String name) {
        return jdbcTemplate.query(SELECT_TAG_BY_NAME, tagMapper, name).stream().findAny();
    }
}
