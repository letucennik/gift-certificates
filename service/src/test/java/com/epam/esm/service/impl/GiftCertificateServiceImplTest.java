package com.epam.esm.service.impl;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.InvalidEntityParameterException;
import com.epam.esm.exception.NoSuchEntityException;
import com.epam.esm.repository.CertificateTagRepository;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.query.SortContext;
import com.epam.esm.validator.Validator;
import com.epam.esm.validator.impl.GiftCertificateValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GiftCertificateServiceImplTest {

    private static final long ID = 1;

    private GiftCertificate certificateToCreate;
    private GiftCertificate secondCertificate;

    private GiftCertificateDto certificateDto;
    private GiftCertificateDto secondCertificateDto;

    @Mock
    private GiftCertificateRepository giftCertificateRepository;
    @Mock
    private CertificateTagRepository certificateTagRepository;
    @Mock
    private TagRepository tagRepository;

    private Validator<GiftCertificate> certificateValidator;
    @Mock
    private Validator<Tag> tagValidator;
    @Mock
    private Validator<SortContext> sortContextValidator;

    private Tag firstTag;
    private Tag secondTag;

    private SortContext sortContext;

    private GiftCertificateServiceImpl giftCertificateService;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
        certificateToCreate = new GiftCertificate(ID, "name",
                "description", BigDecimal.TEN, LocalDateTime.now(), LocalDateTime.now(), 5);
        secondCertificate = new GiftCertificate(2, "second", "lala", BigDecimal.TEN, LocalDateTime.now(), LocalDateTime.now(), 5);
        firstTag = new Tag(1, "1");
        secondTag = new Tag(2, "2");
        sortContext = new SortContext(Collections.singletonList("name"), Collections.singletonList(SortContext.OrderType.DESC));
        certificateDto = new GiftCertificateDto(certificateToCreate, Arrays.asList(firstTag, secondTag));
        secondCertificateDto = new GiftCertificateDto(secondCertificate, new ArrayList<>());
        certificateValidator = Mockito.mock(GiftCertificateValidator.class);
        giftCertificateService = new GiftCertificateServiceImpl(giftCertificateRepository, certificateTagRepository, tagRepository, certificateValidator, tagValidator, sortContextValidator);
    }

    @Test
    void testShouldCreate() {
        when(certificateValidator.isValid(any())).thenReturn(true);
        when(tagValidator.isValid(any())).thenReturn(true);
        when(tagRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(giftCertificateRepository.create(any())).thenReturn(ID);
        assertEquals(ID, giftCertificateService.create(certificateDto).getId());
    }

    @Test
    void testCreateShouldThrowInvalidEntityParameterException() {
        when(certificateValidator.isValid(any())).thenReturn(false);
        assertThrows(InvalidEntityParameterException.class, () -> giftCertificateService.create(certificateDto));
    }

    @Test
    void testShouldRead() {
        when(giftCertificateRepository.read(anyLong())).thenReturn(Optional.of(certificateToCreate));
        assertEquals(Optional.of(certificateToCreate).get().getId(), giftCertificateService.read(ID).getId());
        verify(giftCertificateRepository).read(ID);
    }

    @Test
    void testReadShouldThrowNoSuchEntityException() {
        when(giftCertificateRepository.read(anyLong())).thenReturn(Optional.empty());
        assertThrows(NoSuchEntityException.class, () -> giftCertificateService.read(ID));
    }

    @Test
    void testShouldUpdate() {
        when(giftCertificateRepository.read(anyLong())).thenReturn(Optional.of(certificateToCreate));
        when(tagRepository.findByName(anyString())).thenReturn(Optional.of(secondTag));
        when(((GiftCertificateValidator) certificateValidator).isNameValid(anyString())).thenReturn(true);
        when(((GiftCertificateValidator) certificateValidator).isDescriptionValid(anyString())).thenReturn(true);
        when(((GiftCertificateValidator) certificateValidator).isDurationValid(anyInt())).thenReturn(true);
        when(((GiftCertificateValidator) certificateValidator).isPriceValid(any())).thenReturn(true);
        assertEquals(certificateDto.getId(), giftCertificateService.update(ID, certificateDto).getId());
        verify(giftCertificateRepository).update(anyLong(), any());
    }

    @Test
    void testUpdateShouldThrowNoSuchEntityException() {
        when(giftCertificateRepository.read(anyLong())).thenReturn(Optional.empty());
        assertThrows(NoSuchEntityException.class, () -> giftCertificateService.update(ID, certificateDto));
    }


    @Test
    void testShouldFindByParametersAll() {
        when(sortContextValidator.isValid(any())).thenReturn(true);
        when(giftCertificateRepository.findByParameters(anyString(), anyString(), any())).thenReturn(Collections.singletonList(secondCertificate));
        assertEquals(Collections.singletonList(secondCertificateDto), giftCertificateService.findByParameters("tag 1", "certificate", sortContext));
    }


    @Test
    void testShouldDelete() {
        when(giftCertificateRepository.read(anyLong())).thenReturn(Optional.of(certificateToCreate));
        giftCertificateService.delete(ID);
        verify(giftCertificateRepository).delete(ID);
    }

    @Test
    void testDeleteShouldThrowNoSuchEntityException() {
        when(giftCertificateRepository.read(anyLong())).thenReturn(Optional.empty());
        assertThrows(NoSuchEntityException.class, () -> giftCertificateService.delete(ID));
    }
}