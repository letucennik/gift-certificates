package com.epam.esm.util.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.util.Field;
import com.epam.esm.util.SetterStrategy;

public class NameSetterStrategy extends SetterStrategy {

    public NameSetterStrategy(GiftCertificate certificate) {
        super(certificate);
    }

    @Override
    public Field getField() {
        return Field.NAME;
    }

    @Override
    public void setField(Object value) {
        certificate.setName((String) value);
    }

}
