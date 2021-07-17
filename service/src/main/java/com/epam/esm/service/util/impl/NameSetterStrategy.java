package com.epam.esm.service.util.impl;

import com.epam.esm.repository.entity.GiftCertificate;
import com.epam.esm.service.util.Field;
import com.epam.esm.service.util.SetterStrategy;

public class NameSetterStrategy implements SetterStrategy {

    @Override
    public Field getField() {
        return Field.NAME;
    }

    @Override
    public void setField(GiftCertificate certificate, Object value) {
        certificate.setName((String) value);
    }

}
