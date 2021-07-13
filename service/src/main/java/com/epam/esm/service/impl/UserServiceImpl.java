package com.epam.esm.service.impl;

import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.dto.mapper.Mapper;
import com.epam.esm.repository.entity.User;
import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.exception.InvalidParameterException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.UserService;
import com.epam.esm.service.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    public static final String USER_NOT_FOUND = "user.not.found";

    private final UserRepository userRepository;
    private final Validator<UserDto> userValidator;
    private final Mapper<User, UserDto> userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, Validator<UserDto> userValidator, Mapper<User, UserDto> userMapper) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public UserDto create(UserDto user) {
        if (!userValidator.isValid(user)) {
            throw new InvalidParameterException("user.invalid");
        }
        String name = user.getName();
        if (userRepository.findByName(name).isPresent()) {
            throw new DuplicateEntityException("user.duplicate");
        }
        User savedUser = userRepository.create(userMapper.toModel(user));
        return userMapper.toDto(savedUser);
    }

    @Override
    public UserDto read(long id) {
        Optional<User> tag = userRepository.read(id);
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
        return userRepository.getAll(pageRequest)
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }
}
