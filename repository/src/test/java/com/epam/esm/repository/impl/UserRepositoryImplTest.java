package com.epam.esm.repository.impl;

import com.epam.esm.entity.User;
import com.epam.esm.repository.config.TestJdbcConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestJdbcConfig.class})
@Transactional
class UserRepositoryImplTest {

    private User userToCreate;
    private final User firstUser = new User(1L, "user 1");
    private final User secondUser = new User(2L, "user 2");
    private final User thirdUser = new User(3L, "user 3");

    private List<User> allUsers;

    @Autowired
    private UserRepositoryImpl userRepository;

    @BeforeEach
    void init() {
        userToCreate = new User("user 0");
        allUsers = Arrays.asList(firstUser, secondUser, thirdUser);
    }

    @Test
    void testShouldCreate() {
        assertNotNull(userRepository.create(userToCreate));
    }

    @Test
    void testShouldFind() {
        Optional<User> user=userRepository.find(1L);
        assertTrue(user.isPresent());
        assertEquals(firstUser,user.get());
    }

    @Test
    void testShouldGetAll() {
        assertEquals(allUsers,userRepository.getAll(PageRequest.of(0,25)));
    }
}