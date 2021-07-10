package com.epam.esm.util.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.util.Field;
import com.epam.esm.util.SetterStrategy;

public class DurationSetterStrategy extends SetterStrategy {

    public DurationSetterStrategy(GiftCertificate certificate) {
        super(certificate);
    }

    @Override
    public Field getField() {
        return Field.DURATION;
    }

    @Override
    public void setField(Object value) {
        certificate.setDuration((Integer) value);
    }

}
