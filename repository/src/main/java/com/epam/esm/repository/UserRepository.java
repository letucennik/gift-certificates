package com.epam.esm.repository;

import com.epam.esm.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds User by name.
     *
     * @param name User name to find
     * @return Optional of found user
     */
    Optional<User> findByName(String name);

    /**
     * Finds User by email.
     *
     * @param email User name to find
     * @return Optional of found user
     */
    Optional<User> findByEmail(String email);

}
