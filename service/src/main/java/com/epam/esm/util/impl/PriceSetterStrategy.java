package com.epam.esm.util.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.util.Field;
import com.epam.esm.util.SetterStrategy;

import java.math.BigDecimal;

public class PriceSetterStrategy extends SetterStrategy {

    public PriceSetterStrategy(GiftCertificate certificate) {
        super(certificate);
    }

    @Override
    public Field getField() {
        return Field.PRICE;
    }

    @Override
    public void setField(Object value) {
        certificate.setPrice((BigDecimal) value);
    }

}
