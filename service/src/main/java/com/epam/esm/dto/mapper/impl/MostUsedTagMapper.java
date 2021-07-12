package com.epam.esm.dto.mapper.impl;

import com.epam.esm.dto.MostUsedTagDto;
import com.epam.esm.dto.mapper.Mapper;
import com.epam.esm.entity.MostWidelyUsedTag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class MostUsedTagMapper implements Mapper<MostWidelyUsedTag, MostUsedTagDto> {

    private final ModelMapper mapper;

    @Autowired
    public MostUsedTagMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public MostWidelyUsedTag toModel(MostUsedTagDto dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, MostWidelyUsedTag.class);
    }

    @Override
    public MostUsedTagDto toDto(MostWidelyUsedTag model) {
        return Objects.isNull(model) ? null : mapper.map(model, MostUsedTagDto.class);
    }
}
