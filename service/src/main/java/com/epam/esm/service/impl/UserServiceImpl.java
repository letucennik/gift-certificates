package com.epam.esm.service.impl;

import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.mapper.Mapper;
import com.epam.esm.entity.User;
import com.epam.esm.exception.DuplicateEntityException;
import com.epam.esm.exception.InvalidParameterException;
import com.epam.esm.exception.NoSuchEntityException;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.UserService;
import com.epam.esm.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        return userMapper.toDto(userRepository.create(userMapper.toModel(user)));
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
        List<UserDto> result=new ArrayList<>();
        userRepository.getAll(pageRequest).forEach(x->result.add(userMapper.toDto(x)));
        return result;
    }
}
