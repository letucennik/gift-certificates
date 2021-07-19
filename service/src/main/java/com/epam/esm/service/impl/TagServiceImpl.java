package com.epam.esm.service.impl;

import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.repository.exception.DAOException;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.mapper.Mapper;
import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.exception.InvalidParameterException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {

    public static final String TAG_NOT_FOUND = "tag.not.found";

    private final TagRepository tagRepository;
    private final Validator<TagDto> tagValidator;
    private final Mapper<Tag, TagDto> tagMapper;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository,
                          Validator<TagDto> tagValidator,
                          Mapper<Tag, TagDto> tagMapper) {
        this.tagRepository = tagRepository;
        this.tagValidator = tagValidator;
        this.tagMapper = tagMapper;
    }

    @Override
    @Transactional
    public TagDto create(TagDto tag) {
        if (!tagValidator.isValid(tag)) {
            throw new InvalidParameterException("tag.invalid");
        }
        String name = tag.getName();
        if (tagRepository.findByName(name).isPresent()) {
            throw new DuplicateEntityException("tag.duplicate");
        }
        Tag savedTag = tagRepository.save(tagMapper.toModel(tag));
        return tagMapper.toDto(savedTag);
    }

    @Override
    public TagDto read(long id) {
        Optional<Tag> tag = tagRepository.findById(id);
        return tagMapper.toDto(tag.orElseThrow(() -> new NoSuchEntityException(TAG_NOT_FOUND)));
    }

    @Override
    @Transactional
    public void delete(long id) {
        Tag tag = tagRepository.findById(id).orElseThrow(()->new NoSuchEntityException(TAG_NOT_FOUND));
        tagRepository.delete(tag);
    }

    @Override
    public TagDto getMostWidelyUsedTag() {
        Tag mostWidelyUsedTag;
        try {
            mostWidelyUsedTag = tagRepository.getMostWidelyUsedTag();
        } catch (DAOException e) {
            throw new NoSuchEntityException(TAG_NOT_FOUND);
        }
        return tagMapper.toDto(mostWidelyUsedTag);
    }
}
