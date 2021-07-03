package com.epam.esm.dto.mapper;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class TagMapper {

    private final ModelMapper mapper;

    @Autowired
    public TagMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public Tag toModel(TagDto dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, Tag.class);
    }

    public TagDto toDTO(Tag model) {
        return Objects.isNull(model) ? null : mapper.map(model, TagDto.class);
    }
}
