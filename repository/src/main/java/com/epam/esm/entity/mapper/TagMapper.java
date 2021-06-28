package com.epam.esm.entity.mapper;

import com.epam.esm.entity.Tag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class TagMapper implements RowMapper<Tag> {

    private static final String ID = "id";
    private static final String NAME = "name";

    @Override
    public Tag mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Tag(resultSet.getLong(ID), resultSet.getString(NAME));
    }
}
