package com.epam.esm.service.util.impl;

import com.epam.esm.repository.entity.GiftCertificate;
import com.epam.esm.service.util.Field;
import com.epam.esm.service.util.SetterStrategy;

import java.time.Duration;

public class DurationSetterStrategy implements SetterStrategy {

    @Override
    public Field getField() {
        return Field.DURATION;
    }

    @Override
    public void setField(GiftCertificate certificate, Object value) {
        Long duration = (Long) value;
        certificate.setDuration(Duration.ofDays(duration));
    }

}
