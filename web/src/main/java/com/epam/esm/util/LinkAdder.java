package com.epam.esm.util;

import org.springframework.hateoas.RepresentationModel;

public interface LinkAdder<T extends RepresentationModel<T>> {

    void addLinks(T entity);
}
