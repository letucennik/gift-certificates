package com.epam.esm.service;

import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.DuplicateEntityException;
import com.epam.esm.exception.InvalidParameterException;
import com.epam.esm.exception.NoSuchEntityException;

import java.util.List;

public interface UserService {

    /**
     * Creates new user.
     *
     * @param user User to create
     * @return created user
     * @throws InvalidParameterException when user parameters are invalid
     * @throws DuplicateEntityException  when user with such name already exists
     */
    UserDto create(UserDto user);

    /**
     * Gets User by id.
     *
     * @param id User id to search
     * @return foundUser
     * @throws NoSuchEntityException when User is not found
     */
    UserDto read(long id);

    /**
     * Gets all Users.
     *
     * @param page page number of Users
     * @param size page size
     * @return List of all Users
     * @throws InvalidParameterException when page or size params are invalid
     */
    List<UserDto> getAll(int page, int size);
}