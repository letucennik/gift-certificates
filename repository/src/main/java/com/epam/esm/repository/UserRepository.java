package com.epam.esm.repository;

import com.epam.esm.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    /**
     * Creates new user.
     *
     * @param user User to create
     * @return created user
     */
    User create(User user);

    /**
     * Finds user by id.
     *
     * @param id User id to find
     * @return Optional of found user
     */
    Optional<User> read(long id);

    /**
     * Finds User by name.
     *
     * @param name Tag name to find
     * @return Optional of found user
     */
    Optional<User> findByName(String name);

    /**
     * Finds all Users.
     *
     * @param pageable object with pagination info(page number, page size)
     * @return List of found users
     */
    List<User> getAll(Pageable pageable);

}
