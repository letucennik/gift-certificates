package com.epam.esm.service.impl;

import com.epam.esm.repository.UserRepository;
import com.epam.esm.repository.entity.User;
import com.epam.esm.repository.entity.UserRole;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.dto.mapper.Mapper;
import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.exception.InvalidParameterException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.validator.impl.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    public static final String USER_NOT_FOUND = "user.not.found";
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final Mapper<User, UserDto> userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserValidator userValidator, Mapper<User, UserDto> userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserDto register(UserDto user) {
        validateUser(user);
        if (userRepository.findByName(user.getName()).isPresent()) {
            throw new DuplicateEntityException("user.duplicate");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new DuplicateEntityException("user.duplicate.email");
        }
        user.setUserRole(UserRole.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(userMapper.toModel(user));
        return userMapper.toDto(savedUser);
    }

    @Override
    public UserDto read(long id) {
        Optional<User> tag = userRepository.findById(id);
        return userMapper.toDto(tag.orElseThrow(() -> new NoSuchEntityException(USER_NOT_FOUND)));
    }

    @Override
    public List<UserDto> getAll(int page, int size) {
        Pageable pageRequest;
        try {
            pageRequest = PageRequest.of(page, size);
        } catch (IllegalArgumentException e) {
            throw new InvalidParameterException("pagination.invalid");
        }
        return userRepository.findAll(pageRequest)
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findByName(String name) {
        User user = userRepository.findByName(name).orElseThrow(() -> new NoSuchEntityException(USER_NOT_FOUND));
        return userMapper.toDto(user);
    }

    private void validateUser(UserDto userDto) {
        if (!userValidator.isNameValid(userDto.getName())) {
            throw new InvalidParameterException("user.name.invalid");
        }
        if (!userValidator.isEmailValid(userDto.getEmail())) {
            throw new InvalidParameterException("user.email.invalid");
        }
        if (!userValidator.isPasswordValid(userDto.getPassword())) {
            throw new InvalidParameterException("user.password.invalid");
        }
    }


}
