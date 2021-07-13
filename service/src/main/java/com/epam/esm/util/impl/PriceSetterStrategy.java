package com.epam.esm.util.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.util.Field;
import com.epam.esm.util.SetterStrategy;

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
