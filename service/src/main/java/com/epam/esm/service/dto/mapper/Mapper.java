package com.epam.esm.service.dto.mapper;

public interface Mapper<T, S> {

    T toModel(S dto);

    S toDto(T model);
}
