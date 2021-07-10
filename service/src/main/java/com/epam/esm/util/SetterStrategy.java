package com.epam.esm.util;

import com.epam.esm.entity.GiftCertificate;

public abstract class SetterStrategy {

    protected GiftCertificate certificate;

    public SetterStrategy(GiftCertificate certificate) {
        this.certificate = certificate;
    }

    public abstract Field getField();

    public abstract void setField(Object value);
}
