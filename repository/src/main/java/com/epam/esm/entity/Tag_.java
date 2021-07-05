package com.epam.esm.entity;

import com.epam.esm.entity.Tag;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Tag.class)
public class Tag_ {

    public static volatile SingularAttribute<Tag, Long> id;
    public static volatile SingularAttribute<Tag, String> name;
}
