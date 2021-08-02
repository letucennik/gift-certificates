package com.epam.esm.service.validator.impl;

import com.epam.esm.service.constant.ServiceConstant;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.validator.Validator;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public boolean isEmailValid(String email) {
        Pattern pattern = Pattern.compile(ServiceConstant.EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return isNameValid(email) && matcher.matches();
    }

    public boolean isPasswordValid(String password) {
        Pattern pattern = Pattern.compile(ServiceConstant.PASSWORD_REGEX);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public boolean isNotNull(UserDto userDto) {
        return userDto != null;
    }

    public boolean isIdValid(long id) {
        return id > 0;
    }
}
