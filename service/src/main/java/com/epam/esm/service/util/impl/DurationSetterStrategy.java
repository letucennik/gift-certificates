package com.epam.esm.service.util.impl;

import com.epam.esm.repository.entity.GiftCertificate;
import com.epam.esm.service.util.SetterStrategy;
import com.epam.esm.service.util.Field;

public class DurationSetterStrategy implements SetterStrategy {

    @Override
    public Field getField() {
        return Field.DURATION;
    }

    @Override
    public void setField(GiftCertificate certificate, Object value) {
        certificate.setDuration((Integer) value);
    }

}
