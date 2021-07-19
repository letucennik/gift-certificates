package com.epam.esm.service.util.impl;

import com.epam.esm.repository.entity.GiftCertificate;
import com.epam.esm.service.util.Field;
import com.epam.esm.service.util.SetterStrategy;

public class DescriptionSetterStrategy implements SetterStrategy {


    @Override
    public Field getField() {
        return Field.DESCRIPTION;
    }

    @Override
    public void setField(GiftCertificate certificate, Object value) {
        certificate.setDescription((String) value);
    }

}
