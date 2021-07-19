package com.epam.esm.service.validator.impl;

import com.epam.esm.repository.entity.GiftCertificate;
import com.epam.esm.service.validator.Validator;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class GiftCertificateValidator implements Validator<GiftCertificate> {

    private static final int NAME_MAX_LENGTH = 305;
    private static final int NAME_MIN_LENGTH = 1;
    private static final int DESCRIPTION_MAX_LENGTH = 2000;
    private static final int DESCRIPTION_MIN_LENGTH = 1;
    private static final int DURATION_MIN_VALUE = 1;
    private static final BigDecimal PRICE_MIN_VALUE = BigDecimal.ONE;
    private static final BigDecimal PRICE_MAX_VALUE = new BigDecimal(Integer.MAX_VALUE);

    @Override
    public boolean isValid(GiftCertificate giftCertificate) {
        return isNameValid(giftCertificate.getName()) &&
                isDescriptionValid(giftCertificate.getDescription()) &&
                isDurationValid(giftCertificate.getDuration().toDays()) && isPriceValid(giftCertificate.getPrice());
    }

    public boolean isNameValid(String name) {
        if (name == null) {
            return false;
        }
        return name.length() >= NAME_MIN_LENGTH && name.length() <= NAME_MAX_LENGTH;
    }

    public boolean isDescriptionValid(String description) {
        if (description == null) {
            return false;
        }
        return description.length() >= DESCRIPTION_MIN_LENGTH
                && description.length() <= DESCRIPTION_MAX_LENGTH;
    }

    public boolean isPriceValid(BigDecimal price) {
        if (price == null) {
            return false;
        }
        return price.compareTo(PRICE_MIN_VALUE) >= 0 &&
                price.compareTo(PRICE_MAX_VALUE) <= 0;
    }

    public boolean isDurationValid(long duration) {
        return duration >= DURATION_MIN_VALUE;
    }
}
