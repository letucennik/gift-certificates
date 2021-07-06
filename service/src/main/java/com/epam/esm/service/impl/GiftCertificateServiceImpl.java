package com.epam.esm.service.impl;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.mapper.GiftCertificateMapper;
import com.epam.esm.dto.mapper.TagMapper;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.InvalidParameterException;
import com.epam.esm.exception.InvalidSortParameterException;
import com.epam.esm.exception.NoSuchEntityException;
import com.epam.esm.repository.CertificateTagRepository;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.query.SortContext;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.validator.Validator;
import com.epam.esm.validator.impl.GiftCertificateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    public static final String CERTIFICATE_NOT_FOUND = "certificate.not.found";
    public static final String NAME_FIELD = "name";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String PRICE_FIELD = "price";
    public static final String DURATION_FIELD = "duration";

    private final GiftCertificateRepository giftCertificateRepository;
    private final TagRepository tagRepository;
    private final CertificateTagRepository certificateTagRepository;

    private final Validator<GiftCertificate> giftCertificateValidator;
    private final Validator<TagDto> tagValidator;
    private final Validator<SortContext> sortContextValidator;

    private final GiftCertificateMapper certificateMapper;
    private final TagMapper tagMapper;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepository giftCertificateRepository, CertificateTagRepository certificateTagRepository,
                                      TagRepository tagRepository,
                                      Validator<GiftCertificate> giftCertificateValidator,
                                      Validator<TagDto> tagValidator, Validator<SortContext> sortContextValidator, GiftCertificateMapper mapper,
                                      TagMapper tagMapper) {
        this.giftCertificateRepository = giftCertificateRepository;
        this.certificateTagRepository = certificateTagRepository;
        this.tagRepository = tagRepository;
        this.giftCertificateValidator = giftCertificateValidator;
        this.tagValidator = tagValidator;
        this.sortContextValidator = sortContextValidator;
        this.certificateMapper = mapper;
        this.tagMapper = tagMapper;
    }

    @Override
    @Transactional
    public GiftCertificateDto create(GiftCertificateDto giftCertificateDto) {
        GiftCertificate giftCertificate = certificateMapper.toModel(giftCertificateDto);
        Set<TagDto> tags = giftCertificateDto.getTags();
        validateGiftCertificate(giftCertificate);
        validateTags(tags);
        giftCertificateDto.setCreateDate(LocalDateTime.now());
        giftCertificateDto.setLastUpdateDate(LocalDateTime.now());
        long certificateId = giftCertificateRepository.create(certificateMapper.toModel(giftCertificateDto));
        for (TagDto tagDto : tags) {
            Optional<Tag> tagOptional = tagRepository.findByName(tagDto.getName());
            long tagId = tagOptional.map(Tag::getId).orElseGet(() -> tagRepository.create(tagMapper.toModel(tagDto)));
            tagDto.setId(tagId);
            certificateTagRepository.create(certificateId, tagId);
        }
        GiftCertificateDto dto = certificateMapper.toDTO(giftCertificateRepository.read(certificateId).get());
        dto.setTags(tags);
        return dto;
    }

    @Override
    public GiftCertificateDto read(long id) {
        Optional<GiftCertificate> giftCertificate = giftCertificateRepository.read(id);
        return certificateMapper.toDTO(giftCertificate.orElseThrow(() -> new NoSuchEntityException(CERTIFICATE_NOT_FOUND)));
    }

    @Override
    @Transactional
    public GiftCertificateDto update(long id, GiftCertificateDto dto) {
        GiftCertificate giftCertificate = certificateMapper.toModel(dto);
        GiftCertificate sourceCertificate = giftCertificateRepository.read(id).orElseThrow(NoSuchEntityException::new);
        setUpdatedFields(sourceCertificate, findUpdateInfo(giftCertificate));
        sourceCertificate.setLastUpdateDate(LocalDateTime.now());
        giftCertificateRepository.update(sourceCertificate);
        Set<TagDto> tags = dto.getTags();
        if (tags != null) {
            updateTags(id, tags);
        }
        return certificateMapper.toDTO(sourceCertificate);
    }

    private void setUpdatedFields(GiftCertificate certificate, Map<String, Object> updateInfo) {
        updateInfo.forEach((key, value) -> {
            switch (key) {
                case NAME_FIELD:
                    certificate.setName((String) value);
                    break;
                case DESCRIPTION_FIELD:
                    certificate.setDescription((String) value);
                    break;
                case PRICE_FIELD:
                    certificate.setPrice((BigDecimal) value);
                    break;
                case DURATION_FIELD:
                    certificate.setDuration((Integer) value);
                    break;
            }
        });
    }

    private Map<String, Object> findUpdateInfo(GiftCertificate certificate) {
        Map<String, Object> updateInfo = new HashMap<>();
        GiftCertificateValidator giftCertificateValidator =
                (GiftCertificateValidator) this.giftCertificateValidator;
        String name = certificate.getName();
        if (name != null) {
            if (!giftCertificateValidator.isNameValid(name)) {
                throw new InvalidParameterException("certificate.name.invalid");
            }
            updateInfo.put(NAME_FIELD, name);
        }
        String description = certificate.getDescription();
        if (description != null) {
            if (!giftCertificateValidator.isDescriptionValid(description)) {
                throw new InvalidParameterException("certificate.description.invalid");
            }
            updateInfo.put(DESCRIPTION_FIELD, description);
        }
        BigDecimal price = certificate.getPrice();
        if (price != null) {
            if (!giftCertificateValidator.isPriceValid(price)) {
                throw new InvalidParameterException("certificate.price.invalid");
            }
            updateInfo.put(PRICE_FIELD, price);
        }
        int duration = certificate.getDuration();
        if (duration != 0) {
            if (!giftCertificateValidator.isDurationValid(duration)) {
                throw new InvalidParameterException("certificate.duration.invalid");
            }
            updateInfo.put(DURATION_FIELD, duration);
        }
        return updateInfo;
    }

    private void updateTags(long certificateId, Set<TagDto> tags) {
        for (TagDto tagDto : tags) {
            String tagName = tagDto.getName();
            Optional<Tag> tagOptional = tagRepository.findByName(tagName);
            Tag current = tagOptional.orElseGet(() -> createTag(new Tag(tagDto.getId(), tagDto.getName())));
            if (!certificateTagRepository.findTagsIdByCertificateId(certificateId).contains(current.getId())) {
                certificateTagRepository.create(certificateId, current.getId());
            }
        }
    }

    private Tag createTag(Tag tag) {
        tagRepository.create(tag);
        return tagRepository.findByName(tag.getName()).orElseThrow(NoSuchEntityException::new);
    }

    @Override
    public List<GiftCertificateDto> findByParameters(String tagName, String partValue, SortContext sortContext, int page, int size) {
        Pageable pageRequest;
        try {
            pageRequest = PageRequest.of(page, size);
        } catch (IllegalArgumentException | MethodArgumentTypeMismatchException e) {
            throw new InvalidParameterException("pagination.invalid");
        }
        if (isSortContextPassed(sortContext)) {
            validateSortContext(sortContext);
        }
        List<GiftCertificateDto> certificates = new ArrayList<>();
        giftCertificateRepository.findByParameters(tagName, partValue, sortContext, pageRequest).forEach(giftCertificate ->
                certificates.add(certificateMapper.toDTO(giftCertificate)));
        return certificates;
    }

    private boolean isSortContextPassed(SortContext sortContext) {
        return sortContext != null && sortContext.getSortColumns() != null;
    }


    @Override
    @Transactional
    public void delete(long id) {
        Optional<GiftCertificate> certificateOptional = giftCertificateRepository.read(id);
        if (!certificateOptional.isPresent()) {
            throw new NoSuchEntityException(CERTIFICATE_NOT_FOUND);
        }
        giftCertificateRepository.delete(id);
    }

    private void validateGiftCertificate(GiftCertificate certificate) {
        if (!giftCertificateValidator.isValid(certificate)) {
            throw new InvalidParameterException("certificate.invalid");
        }
    }

    private void validateTags(Set<TagDto> tags) {
        if (!tags.stream().allMatch(tagValidator::isValid)) {
            throw new InvalidParameterException("tag.invalid");
        }
    }

    private void validateSortContext(SortContext context) {
        if (!sortContextValidator.isValid(context)) {
            throw new InvalidSortParameterException("sort.context.invalid");
        }
    }
}
