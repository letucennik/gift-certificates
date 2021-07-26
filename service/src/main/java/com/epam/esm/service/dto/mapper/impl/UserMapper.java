package com.epam.esm.service.dto.mapper.impl;

import com.epam.esm.repository.entity.User;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.dto.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserMapper implements Mapper<User, UserDto> {

    private final ModelMapper mapper;

    @Autowired
    public UserMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public User toModel(UserDto dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, User.class);
    }

    @Override
    public UserDto toDto(User model) {
        return Objects.isNull(model) ? null : mapper.map(model, UserDto.class);
    }
}
