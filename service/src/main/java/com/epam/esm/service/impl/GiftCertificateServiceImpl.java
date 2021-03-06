package com.epam.esm.service.impl;

import com.epam.esm.repository.CertificateTagRepository;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.entity.GiftCertificate;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.repository.query.SearchSpecification;
import com.epam.esm.repository.query.SortContext;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.mapper.impl.GiftCertificateMapper;
import com.epam.esm.service.dto.mapper.impl.TagMapper;
import com.epam.esm.service.exception.InvalidParameterException;
import com.epam.esm.service.exception.InvalidSortParameterException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.util.Field;
import com.epam.esm.service.util.SetterStrategy;
import com.epam.esm.service.validator.Validator;
import com.epam.esm.service.validator.impl.GiftCertificateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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

    private final GiftCertificateValidator giftCertificateValidator;
    private final Validator<TagDto> tagValidator;
    private final Validator<SortContext> sortContextValidator;

    private final GiftCertificateMapper certificateMapper;
    private final TagMapper tagMapper;
    private final Map<Field, SetterStrategy> setterMap;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepository giftCertificateRepository, CertificateTagRepository certificateTagRepository,
                                      TagRepository tagRepository,
                                      GiftCertificateValidator giftCertificateValidator,
                                      Validator<TagDto> tagValidator, Validator<SortContext> sortContextValidator, GiftCertificateMapper mapper,
                                      TagMapper tagMapper,
                                      Map<Field, SetterStrategy> setterMap) {
        this.giftCertificateRepository = giftCertificateRepository;
        this.certificateTagRepository = certificateTagRepository;
        this.tagRepository = tagRepository;
        this.giftCertificateValidator = giftCertificateValidator;
        this.tagValidator = tagValidator;
        this.sortContextValidator = sortContextValidator;
        this.certificateMapper = mapper;
        this.tagMapper = tagMapper;
        this.setterMap = setterMap;
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
        for (TagDto tagDto : tags) {
            Optional<Tag> tagOptional = tagRepository.findByName(tagDto.getName());
            long tagId = tagOptional.map(Tag::getId).orElseGet(() -> tagRepository.save(tagMapper.toModel(tagDto)).getId());
            tagDto.setId(tagId);
        }
        GiftCertificate certificate = giftCertificateRepository.save(certificateMapper.toModel(giftCertificateDto));
        tags.forEach(x -> certificateTagRepository.create(certificate.getId(), x.getId()));
        return certificateMapper.toDto(certificate);
    }

    @Override
    public GiftCertificateDto read(long id) {
        return giftCertificateRepository.findById(id)
                .map(certificateMapper::toDto)
                .orElseThrow(() -> new NoSuchEntityException(CERTIFICATE_NOT_FOUND));
    }

    @Override
    @Transactional
    public GiftCertificateDto update(long id, GiftCertificateDto dto) {
        GiftCertificate giftCertificate = certificateMapper.toModel(dto);
        GiftCertificate sourceCertificate = giftCertificateRepository.findById(id).orElseThrow(() -> new NoSuchEntityException(CERTIFICATE_NOT_FOUND));
        setUpdatedFields(sourceCertificate, findUpdateInfo(giftCertificate));
        sourceCertificate.setLastUpdateDate(LocalDateTime.now());
        giftCertificateRepository.save(sourceCertificate);
        Set<TagDto> tags = dto.getTags();
        if (tags != null) {
            updateTags(id, tags);
        }
        return certificateMapper.toDto(sourceCertificate);
    }

    private void setUpdatedFields(GiftCertificate certificate, Map<String, Object> updateInfo) {
        updateInfo.forEach((key, value) -> {
            setterMap.get(Field.valueOf(key.toUpperCase())).setField(certificate, value);
        });
    }

    private Map<String, Object> findUpdateInfo(GiftCertificate certificate) {
        Map<String, Object> updateInfo = new HashMap<>();
        String name = certificate.getName();
        if (name != null) {
            validateGiftCertificateName(name);
            updateInfo.put(NAME_FIELD, name);
        }
        String description = certificate.getDescription();
        if (description != null) {
            validateGiftCertificateDescription(description);
            updateInfo.put(DESCRIPTION_FIELD, description);
        }
        BigDecimal price = certificate.getPrice();
        if (price != null) {
            validateGiftCertificatePrice(price);
            updateInfo.put(PRICE_FIELD, price);
        }
        long duration = certificate.getDuration().toDays();
        if (duration != 0) {
            validateGiftCertificateDuration(duration);
            updateInfo.put(DURATION_FIELD, duration);
        }
        return updateInfo;
    }

    private void updateTags(long certificateId, Set<TagDto> tags) {
        for (TagDto tagDto : tags) {
            String tagName = tagDto.getName();
            Optional<Tag> tagOptional = tagRepository.findByName(tagName);
            Tag current = tagOptional.orElseGet(() -> createTag(new Tag(tagDto.getId(), tagDto.getName())));
            if (!certificateTagRepository.findTagsIdByCertificateId(certificateId).contains(current)) {
                certificateTagRepository.create(certificateId, current.getId());
            }
        }
    }

    private Tag createTag(Tag tag) {
        tagRepository.save(tag);
        return tagRepository.findByName(tag.getName()).orElseThrow(NoSuchEntityException::new);
    }

    @Override
    public List<GiftCertificateDto> findByParameters(List<String> tagNames, String partValue, SortContext sortContext, int page, int size) {
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
        giftCertificateRepository.findAll(new SearchSpecification(tagNames, partValue, sortContext), pageRequest).getContent()
                .forEach(giftCertificate -> certificates.add(certificateMapper.toDto(giftCertificate)));
        return certificates;
    }

    private boolean isSortContextPassed(SortContext sortContext) {
        return sortContext != null && sortContext.getSortColumns() != null;
    }


    @Override
    @Transactional
    public void delete(long id) {
        GiftCertificate certificate = giftCertificateRepository.findById(id).orElseThrow(() -> new NoSuchEntityException(CERTIFICATE_NOT_FOUND));
        giftCertificateRepository.delete(certificate);
    }

    private void validateGiftCertificate(GiftCertificate certificate) {
        validateGiftCertificateName(certificate.getName());
        validateGiftCertificateDescription(certificate.getDescription());
        validateGiftCertificatePrice(certificate.getPrice());
        validateGiftCertificateDuration(certificate.getDuration().toDays());
    }

    private void validateGiftCertificateName(String name) {
        if (!giftCertificateValidator.isNameValid(name)) {
            throw new InvalidParameterException("certificate.name.invalid");
        }
    }

    private void validateGiftCertificateDescription(String description) {
        if (!giftCertificateValidator.isDescriptionValid(description)) {
            throw new InvalidParameterException("certificate.description.invalid");
        }
    }

    private void validateGiftCertificatePrice(BigDecimal price) {
        if (!giftCertificateValidator.isPriceValid(price)) {
            throw new InvalidParameterException("certificate.price.invalid");
        }
    }

    private void validateGiftCertificateDuration(long duration) {
        if (!giftCertificateValidator.isDurationValid(duration)) {
            throw new InvalidParameterException("certificate.duration.invalid");
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
