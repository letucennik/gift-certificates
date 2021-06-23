package com.epam.esm.service.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DuplicateEntityException;
import com.epam.esm.exception.InvalidEntityParameterException;
import com.epam.esm.exception.NoSuchEntityException;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagService;
import com.epam.esm.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {

    public static final String TAG_NOT_FOUND = "tag.not.found";

    private final TagRepository tagRepository;
    private final Validator<Tag> tagValidator;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository, Validator<Tag> tagValidator) {
        this.tagRepository = tagRepository;
        this.tagValidator = tagValidator;
    }

    @Override
    @Transactional
    public Tag create(Tag tag) {
        if (!tagValidator.isValid(tag)) {
            throw new InvalidEntityParameterException("tag.invalid");
        }
        String name = tag.getName();
        if (tagRepository.findByName(name).isPresent()) {
            throw new DuplicateEntityException("tag.duplicate");
        }
        long id = tagRepository.create(tag);
        return tagRepository.read(id).get();
    }

    @Override
    public Tag read(long id) {
        Optional<Tag> tag = tagRepository.read(id);
        return tag.orElseThrow(() -> new NoSuchEntityException(TAG_NOT_FOUND));
    }

    @Override
    @Transactional
    public void delete(long id) {
        Optional<Tag> tag = tagRepository.read(id);
        if (!tag.isPresent()) {
            throw new NoSuchEntityException(TAG_NOT_FOUND);
        }
        tagRepository.delete(id);
    }
}