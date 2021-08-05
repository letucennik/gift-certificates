package com.epam.esm.service.impl;

import com.epam.esm.repository.UserRepository;
import com.epam.esm.repository.entity.User;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.dto.mapper.Mapper;
import com.epam.esm.service.dto.mapper.impl.UserMapper;
import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.exception.InvalidParameterException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.validator.impl.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Component
class UserServiceImplTest {

    private static final long ID = 1;
    private static User userToCreate;
    private static UserDto userToCreateDto;

    private List<User> allUsers;
    private List<UserDto> allUsersDto;

    @Mock
    private UserRepository userRepository;

    @Spy
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserValidator userValidator;
    @Spy
    private final Mapper<User, UserDto> mapper = new UserMapper(new ModelMapper());

    @InjectMocks
    private UserServiceImpl userService;


    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        userToCreate = new User(ID, "user 0", "password0");
        userToCreateDto = mapper.toDto(userToCreate);
        allUsers = Collections.singletonList(userToCreate);
        allUsersDto = Collections.singletonList(userToCreateDto);
    }

    @Test
    void testCreateShouldCreate() {
        when(userValidator.isIdValid(anyLong())).thenReturn(true);
        when(userValidator.isNameValid(anyString())).thenReturn(true);
        when(userValidator.isEmailValid(anyString())).thenReturn(true);
        when(userValidator.isPasswordValid(anyString())).thenReturn(true);
        when(userRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(userToCreate);
        Long id = userService.register(userToCreateDto).getId();
        assertNotNull(id);
    }

    @Test
    void testCreateShouldThrowInvalidEntityParameterException() {
        when(userValidator.isValid(any())).thenReturn(false);
        assertThrows(InvalidParameterException.class, () -> userService.register(userToCreateDto));
    }

    @Test
    void testCreateShouldThrowDuplicateEntityException() {
        when(userValidator.isIdValid(anyLong())).thenReturn(true);
        when(userValidator.isNameValid(anyString())).thenReturn(true);
        when(userValidator.isEmailValid(anyString())).thenReturn(true);
        when(userValidator.isPasswordValid(anyString())).thenReturn(true);
        when(userRepository.findByName(anyString())).thenReturn(Optional.of(userToCreate));
        assertThrows(DuplicateEntityException.class, () -> userService.register(userToCreateDto));
    }

    @Test
    void testShouldFindById() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userToCreate));
        assertEquals(userToCreateDto, userService.read(ID));
        verify(userRepository).findById(ID);
    }

    @Test
    void testReadShouldThrowNoSuchEntityException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NoSuchEntityException.class, () -> userService.read(ID));
    }

}