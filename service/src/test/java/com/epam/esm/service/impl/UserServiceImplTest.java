package com.epam.esm.service.impl;

import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.mapper.Mapper;
import com.epam.esm.dto.mapper.impl.UserMapper;
import com.epam.esm.entity.User;
import com.epam.esm.exception.DuplicateEntityException;
import com.epam.esm.exception.InvalidParameterException;
import com.epam.esm.exception.NoSuchEntityException;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.validator.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    private static final long ID = 1;
    private static User userToCreate;
    private static UserDto userToCreateDto;

    private List<User> allUsers;
    private List<UserDto> allUsersDto;

    @Mock
    private UserRepository userRepository;
    @Mock
    private Validator<UserDto> userValidator;
    @Spy
    private final Mapper<User, UserDto> mapper = new UserMapper(new ModelMapper());

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        userToCreate = new User(ID, "user 0");
        userToCreateDto = mapper.toDto(userToCreate);
        allUsers = Collections.singletonList(userToCreate);
        allUsersDto = Collections.singletonList(userToCreateDto);
    }

    @Test
    void testCreateShouldCreate() {
        when(userValidator.isValid(any())).thenReturn(true);
        when(userRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(userRepository.create(any())).thenReturn(userToCreate);
        Long id = userService.create(userToCreateDto).getId();
        assertNotNull(id);
        verify(userRepository).create(userToCreate);
    }

    @Test
    void testCreateShouldThrowInvalidEntityParameterException() {
        when(userValidator.isValid(any())).thenReturn(false);
        assertThrows(InvalidParameterException.class, () -> userService.create(userToCreateDto));
    }

    @Test
    void testCreateShouldThrowDuplicateEntityException() {
        when(userValidator.isValid(any())).thenReturn(true);
        when(userRepository.findByName(anyString())).thenReturn(Optional.of(userToCreate));
        assertThrows(DuplicateEntityException.class, () -> userService.create(userToCreateDto));
    }

    @Test
    void testShouldFindById() {
        when(userRepository.read(anyLong())).thenReturn(Optional.of(userToCreate));
        assertEquals(userToCreateDto, userService.read(ID));
        verify(userRepository).read(ID);
    }

    @Test
    void testReadShouldThrowNoSuchEntityException() {
        when(userRepository.read(anyLong())).thenReturn(Optional.empty());
        assertThrows(NoSuchEntityException.class, () -> userService.read(ID));
    }

    @Test
    void shouldFindAll() {
        when(userRepository.getAll(any())).thenReturn(allUsers);
        assertEquals(allUsersDto, userService.getAll(0, 25));
    }

}