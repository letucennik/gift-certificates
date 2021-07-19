package com.epam.esm.service.validator.impl;

import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.validator.Validator;
import org.springframework.stereotype.Component;

@Component
public class TagValidator implements Validator<TagDto> {

    private static final int NAME_MAX_LENGTH = 305;
    private static final int NAME_MIN_LENGTH = 1;

    @Override
    public boolean isValid(TagDto item) {
        String name = item.getName();
        if (name == null) {
            return false;
        }
        return name.length() >= NAME_MIN_LENGTH && name.length() <= NAME_MAX_LENGTH;
    }
}
