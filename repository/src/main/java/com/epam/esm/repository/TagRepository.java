package com.epam.esm.repository;

import com.epam.esm.entity.Tag;

import java.util.Optional;

public interface TagRepository {

    /**
     * Creates new tag.
     *
     * @param tag Tag to create
     * @return created tag
     */
    Tag create(Tag tag);

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
    void delete(long id);

    /**
     * Finds Tag by name.
     *
     * @param name Tag name to find
     * @return Optional of found tag
     */
    Optional<Tag> findByName(String name);

    /**
     * Finds the most widely used tag of a user with the highest cost of all orders
     *
     * @param userId User id
     * @return found tag
     */
    Tag getMostWildlyUsedTag(long userId);
}
