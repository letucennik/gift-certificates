package com.epam.esm.repository;

import com.epam.esm.entity.Tag;

import java.util.Optional;

public interface TagRepository {

    long create(Tag tag);

    Optional<Tag> read(long id);

    int delete(long id);

    Optional<Tag> findByName(String name);
}
