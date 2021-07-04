package com.epam.esm.service;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DuplicateEntityException;
import com.epam.esm.exception.InvalidEntityParameterException;
import com.epam.esm.exception.NoSuchEntityException;

public interface TagService {

    /**
     * Creates new tag.
     *
     * @param tag Tag to create
     * @return created tag
     * @throws InvalidEntityParameterException when tag parameters are invalid
     * @throws DuplicateEntityException        when tag with such name already exists
     */
    TagDto create(TagDto tag);

    /**
     * Gets tag by id.
     *
     * @param id Tag id to search
     * @return found Tag
     * @throws NoSuchEntityException when such tag doesn't exists
     */
    TagDto read(long id);

    /**
     * Deletes tag by id.
     *
     * @param id Tag id to search
     * @throws NoSuchEntityException when such tag doesn't exists
     */
    void delete(long id);
}
