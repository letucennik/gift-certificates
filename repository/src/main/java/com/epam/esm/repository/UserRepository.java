package com.epam.esm.repository;

import com.epam.esm.repository.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
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
     * Method finds all user with pagination
     *
     * @param pageable object for pagination
     * @return Page of User
     */
    Page<User> findAll(Pageable pageable);

}
