package com.epam.esm.service.util.impl;

import com.epam.esm.repository.entity.GiftCertificate;
import com.epam.esm.service.util.Field;
import com.epam.esm.service.util.SetterStrategy;

import java.math.BigDecimal;

public class PriceSetterStrategy implements SetterStrategy {

    @Override
    public Field getField() {
        return Field.PRICE;
    }

    @Override
    public void setField(GiftCertificate certificate, Object value) {
        certificate.setPrice((BigDecimal) value);
    }

}
