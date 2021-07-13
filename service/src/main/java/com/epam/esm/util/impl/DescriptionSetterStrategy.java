package com.epam.esm.util.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.util.Field;
import com.epam.esm.util.SetterStrategy;

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
