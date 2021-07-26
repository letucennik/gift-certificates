package com.epam.esm.service.impl;

import com.epam.esm.repository.CertificateTagRepository;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.entity.GiftCertificate;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.repository.query.SearchSpecification;
import com.epam.esm.repository.query.SortContext;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.mapper.impl.GiftCertificateMapper;
import com.epam.esm.service.dto.mapper.impl.TagMapper;
import com.epam.esm.service.exception.InvalidParameterException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.util.Field;
import com.epam.esm.service.util.SetterStrategy;
import com.epam.esm.service.validator.Validator;
import com.epam.esm.service.validator.impl.GiftCertificateValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
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

    private GiftCertificateValidator certificateValidator;
    @Mock
    private Validator<TagDto> tagValidator;
    @Mock
    private Validator<SortContext> sortContextValidator;
    @Spy
    private Map<Field, SetterStrategy> setterMap;

    @Spy
    private GiftCertificateMapper certificateMapper = new GiftCertificateMapper(new ModelMapper(), new TagMapper(new ModelMapper()));
    @Spy
    private TagMapper tagMapper = new TagMapper(new ModelMapper());

    private Tag firstTag;
    private Tag secondTag;

    private SortContext sortContext;

    private GiftCertificateServiceImpl giftCertificateService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        certificateToCreate = new GiftCertificate(1, "name",
                "description", BigDecimal.TEN, Duration.ofDays(5));
        secondCertificate = new GiftCertificate(2, "second", "lala", BigDecimal.TEN, Duration.ofDays(5));
        firstTag = new Tag(1, "1");
        secondTag = new Tag(2, "2");
        sortContext = new SortContext(Collections.singletonList("name"), Collections.singletonList(SortContext.OrderType.DESC));
        certificateDto = new GiftCertificateDto(certificateToCreate);
        secondCertificateDto = new GiftCertificateDto(secondCertificate);
        certificateValidator = Mockito.mock(GiftCertificateValidator.class);
        giftCertificateService = new GiftCertificateServiceImpl(giftCertificateRepository, certificateTagRepository, tagRepository, certificateValidator, tagValidator, sortContextValidator, certificateMapper, tagMapper, setterMap);
    }

    @Test
    void testShouldCreate() {
        when(certificateValidator.isValid(any())).thenReturn(true);
        when(giftCertificateRepository.save(any())).thenReturn(certificateToCreate);
        when(giftCertificateRepository.findById(anyLong())).thenReturn(Optional.ofNullable(certificateToCreate));
        assertEquals(ID, giftCertificateService.create(certificateDto).getId());
    }

    @Test
    void testCreateShouldThrowInvalidEntityParameterException() {
        when(certificateValidator.isValid(any())).thenReturn(false);
        assertThrows(InvalidParameterException.class, () -> giftCertificateService.create(certificateDto));
    }

    @Test
    void testShouldRead() {
        when(giftCertificateRepository.findById(anyLong())).thenReturn(Optional.of(certificateToCreate));
        assertEquals(Optional.of(certificateToCreate).get().getId(), giftCertificateService.read(ID).getId());
        verify(giftCertificateRepository).findById(ID);
    }

    @Test
    void testReadShouldThrowNoSuchEntityException() {
        when(giftCertificateRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NoSuchEntityException.class, () -> giftCertificateService.read(ID));
    }

    @Test
    void testShouldUpdate() {
        when(giftCertificateRepository.findById(anyLong())).thenReturn(Optional.of(certificateToCreate));
        when(tagRepository.findByName(anyString())).thenReturn(Optional.of(secondTag));
        when((certificateValidator).isNameValid(anyString())).thenReturn(true);
        when((certificateValidator).isDescriptionValid(anyString())).thenReturn(true);
        when((certificateValidator).isDurationValid(any())).thenReturn(true);
        when((certificateValidator).isPriceValid(any())).thenReturn(true);
        assertEquals(certificateDto.getId(), giftCertificateService.update(ID, certificateDto).getId());
        verify(giftCertificateRepository).save(any());
    }

    @Test
    void testUpdateShouldThrowNoSuchEntityException() {
        when(giftCertificateRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NoSuchEntityException.class, () -> giftCertificateService.update(ID, certificateDto));
    }


    @Test
    void testShouldFindByParametersAll() {
        when(sortContextValidator.isValid(any())).thenReturn(true);
        SearchSpecification searchSpecification=new SearchSpecification();
        when(giftCertificateRepository.findAll(searchSpecification.findByParameters())).thenReturn(Collections.singletonList(secondCertificate));
        assertEquals(Collections.singletonList(secondCertificateDto), giftCertificateService.findByParameters(Collections.singletonList("tag1"), "certificate", sortContext, 0, 25));
    }


    @Test
    void testShouldDelete() {
        when(giftCertificateRepository.findById(anyLong())).thenReturn(Optional.of(certificateToCreate));
        giftCertificateService.delete(ID);
        verify(giftCertificateRepository).delete(certificateToCreate);
    }

    @Test
    void testDeleteShouldThrowNoSuchEntityException() {
        when(giftCertificateRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NoSuchEntityException.class, () -> giftCertificateService.delete(ID));
    }
}