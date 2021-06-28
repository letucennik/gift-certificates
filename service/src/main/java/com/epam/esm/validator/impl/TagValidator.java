package com.epam.esm.validator.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.validator.Validator;
import org.springframework.stereotype.Component;

@Component
public class TagValidator implements Validator<Tag> {

    private static final int NAME_MAX_LENGTH = 305;
    private static final int NAME_MIN_LENGTH = 1;

    @Override
    public boolean isValid(Tag item) {
        String name = item.getName();
        if (name == null) {
            return false;
        }
        return name.length() >= NAME_MIN_LENGTH && name.length() <= NAME_MAX_LENGTH;
    }
}
