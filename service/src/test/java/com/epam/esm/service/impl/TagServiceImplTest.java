package com.epam.esm.service.impl;

import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.mapper.impl.TagMapper;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.exception.InvalidParameterException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.validator.Validator;
import com.epam.esm.service.validator.impl.TagValidator;
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
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShouldCreate() {
        when(tagValidator.isValid(any())).thenReturn(true);
        when(tagRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(tagRepository.save(any())).thenReturn(tag);
        Long id = tagService.create(tagDto).getId();
        assertNotNull(id);
        verify(tagRepository).save(tag);
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
        when(tagRepository.findById(anyLong())).thenReturn(Optional.of(tag));
        assertEquals(tagDto, tagService.read(ID));
        verify(tagRepository).findById(ID);
    }

    @Test
    void testReadShouldThrowNoSuchEntityException() {
        when(tagRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NoSuchEntityException.class, () -> tagService.read(ID));
    }

    @Test
    void testShouldDelete() {
        when(tagRepository.findById(anyLong())).thenReturn(Optional.of(tag));
        tagService.delete(ID);
        verify(tagRepository).delete(tag);
    }

    @Test
    void testDeleteShouldThrowNoSuchEntityException() {
        when(tagRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NoSuchEntityException.class, () -> tagService.delete(ID));
    }
}