package com.epam.esm.service.util;

import com.epam.esm.repository.entity.GiftCertificate;

public interface SetterStrategy {

    public abstract Field getField();

    public abstract void setField(GiftCertificate certificate, Object value);
}
