package com.epam.esm.service;

import com.epam.esm.repository.entity.User;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.exception.InvalidParameterException;
import com.epam.esm.service.exception.NoSuchEntityException;

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
    UserDto register(UserDto user);


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

    /**
     * Gets User by name.
     *
     * @param name User name to search
     * @return foundUser
     * @throws NoSuchEntityException when User is not found
     */
    User findByName(String name);
}
