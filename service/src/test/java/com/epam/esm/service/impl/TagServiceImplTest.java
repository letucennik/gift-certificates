package com.epam.esm.service.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DuplicateEntityException;
import com.epam.esm.exception.InvalidEntityParameterException;
import com.epam.esm.exception.NoSuchEntityException;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.impl.TagRepositoryImpl;
import com.epam.esm.service.TagService;
import com.epam.esm.validator.Validator;
import com.epam.esm.validator.impl.TagValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TagServiceImplTest {

    private static final long ID = 1;
    private static Tag tag;

    private TagRepository tagRepository;
    private Validator<Tag> tagValidator;

    private TagService tagService;

    @BeforeEach
    void init() {
        tag = new Tag(ID, "tag");
        tagRepository = Mockito.mock(TagRepositoryImpl.class);
        tagValidator = Mockito.mock(TagValidator.class);
        tagService = new TagServiceImpl(tagRepository, tagValidator);
    }

    @Test
    void testShouldCreate() {
        when(tagValidator.isValid(any())).thenReturn(true);
        when(tagRepository.findByName(anyString())).thenReturn(Optional.empty());
        Long id = tagService.create(tag).getId();
        assertNotNull(id);
        verify(tagRepository).create(tag);
    }

    @Test
    void testCreateShouldThrowInvalidEntityParameterException() {
        when(tagValidator.isValid(any())).thenReturn(false);
        assertThrows(InvalidEntityParameterException.class, () -> tagService.create(tag));
    }

    @Test
    void testCreateShouldThrowDuplicateEntityException() {
        when(tagValidator.isValid(any())).thenReturn(true);
        when(tagRepository.findByName(anyString())).thenReturn(Optional.of(tag));
        assertThrows(DuplicateEntityException.class, () -> tagService.create(tag));
    }

    @Test
    void testShouldFindById() {
        when(tagRepository.read(anyLong())).thenReturn(Optional.of(tag));
        assertEquals(tag, tagService.read(ID));
        verify(tagRepository).read(ID);
    }

    @Test
    void testReadShouldThrowNoSuchEntityException() {
        when(tagRepository.read(anyLong())).thenReturn(Optional.empty());
        assertThrows(NoSuchEntityException.class, () -> tagService.read(ID));
    }

    @Test
    void testShouldDelete() {
        when(tagRepository.read(anyLong())).thenReturn(Optional.of(tag));
        tagService.delete(ID);
        verify(tagRepository).delete(ID);
    }

    @Test
    void testDeleteShouldThrowNoSuchEntityException() {
        when(tagRepository.read(anyLong())).thenReturn(Optional.empty());
        assertThrows(NoSuchEntityException.class, () -> tagService.delete(ID));
    }
}