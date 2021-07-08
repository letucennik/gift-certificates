package com.epam.esm.repository;

import com.epam.esm.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User create(User user);

    Optional<User> find(long id);

    List<User> getAll(Pageable pageable);

}
