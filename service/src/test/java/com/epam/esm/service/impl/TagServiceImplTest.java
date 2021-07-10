package com.epam.esm.service.impl;

import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.mapper.impl.TagMapper;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DuplicateEntityException;
import com.epam.esm.exception.InvalidParameterException;
import com.epam.esm.exception.NoSuchEntityException;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.validator.Validator;
import com.epam.esm.validator.impl.TagValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TagServiceImplTest {

    private static final long ID = 1;
    private static Tag tag;
    private static TagDto tagDto;

    @Mock
    private TagRepository tagRepository;
    @Mock
    private final Validator<TagDto> tagValidator = new TagValidator();
    @Spy
    private final TagMapper mapper = new TagMapper(new ModelMapper());

    @InjectMocks
    private TagServiceImpl tagService;

    @BeforeEach
    void init() {
        tag = new Tag(ID, "tag");
        tagDto = mapper.toDto(tag);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testShouldCreate() {
        when(tagValidator.isValid(any())).thenReturn(true);
        when(tagRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(tagRepository.create(any())).thenReturn(tag);
        Long id = tagService.create(tagDto).getId();
        assertNotNull(id);
        verify(tagRepository).create(tag);
    }

    @Test
    void testCreateShouldThrowInvalidEntityParameterException() {
        when(tagValidator.isValid(any())).thenReturn(false);
        assertThrows(InvalidParameterException.class, () -> tagService.create(tagDto));
    }

    @Test
    void testCreateShouldThrowDuplicateEntityException() {
        when(tagValidator.isValid(any())).thenReturn(true);
        when(tagRepository.findByName(anyString())).thenReturn(Optional.of(tag));
        assertThrows(DuplicateEntityException.class, () -> tagService.create(tagDto));
    }

    @Test
    void testShouldFindById() {
        when(tagRepository.read(anyLong())).thenReturn(Optional.of(tag));
        assertEquals(tagDto, tagService.read(ID));
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