package com.epam.esm.util.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.util.Field;
import com.epam.esm.util.SetterStrategy;

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
