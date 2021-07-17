package com.epam.esm.service.validator.impl;

import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.validator.Validator;
import org.springframework.stereotype.Component;

@Component
public class UserValidator implements Validator<UserDto> {

    private static final int NAME_MAX_LENGTH = 305;
    private static final int NAME_MIN_LENGTH = 1;

    @Override
    public boolean isValid(UserDto item) {
        return isNotNull(item) && isIdValid(item.getId()) && isNameValid(item.getName());
    }

    public boolean isNameValid(String name) {
        if (name == null) {
            return false;
        }
        return name.length() >= NAME_MIN_LENGTH && name.length() <= NAME_MAX_LENGTH;
    }

    public boolean isNotNull(UserDto userDto) {
        return userDto != null;
    }

    public boolean isIdValid(long id) {
        return id > 0;
    }
}
