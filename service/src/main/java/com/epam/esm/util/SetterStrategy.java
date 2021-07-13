package com.epam.esm.util;

import com.epam.esm.entity.GiftCertificate;

public interface SetterStrategy {

    public abstract Field getField();

    public abstract void setField(GiftCertificate certificate, Object value);
}
