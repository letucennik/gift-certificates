package com.epam.esm.repository;

import com.epam.esm.entity.Tag;

import java.util.Optional;

public interface TagRepository {

    /**
     * Creates new tag.
     *
     * @param tag Tag to create
     * @return id of created tag
     */
    long create(Tag tag);

    /**
     * Finds tag by id.
     *
     * @param id Tag id to find
     * @return Optional of found tag
     */
    Optional<Tag> read(long id);

    /**
     * Deletes tag by id.
     *
     * @param id Tag id to delete
     */
    int delete(long id);

    /**
     * Finds Tag by name.
     *
     * @param name Tag name to find
     * @return Optional of found tag
     */
    Optional<Tag> findByName(String name);
}
