package com.epam.esm.dto.mapper;

import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
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
