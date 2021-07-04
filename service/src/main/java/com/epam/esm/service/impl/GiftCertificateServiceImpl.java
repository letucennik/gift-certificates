package com.epam.esm.service.impl;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.mapper.GiftCertificateMapper;
import com.epam.esm.dto.mapper.TagMapper;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.InvalidEntityParameterException;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    public static final String CERTIFICATE_NOT_FOUND = "certificate.not.found";

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
    public GiftCertificate read(long id) {
        Optional<GiftCertificate> giftCertificate = giftCertificateRepository.read(id);
        return giftCertificate.orElseThrow(() -> new NoSuchEntityException(CERTIFICATE_NOT_FOUND));
    }

    @Override
    @Transactional
    public GiftCertificateDto update(long id, GiftCertificateDto dto) {
        GiftCertificate giftCertificate = certificateMapper.toModel(dto);
        if (!giftCertificateRepository.read(id).isPresent()) {
            throw new NoSuchEntityException(CERTIFICATE_NOT_FOUND);
        }
        giftCertificateRepository.update(giftCertificate);
        Set<TagDto> tags = dto.getTags();
        if (tags != null) {
            updateTags(id, tags);
        }
        return certificateMapper.toDTO(giftCertificate);
    }

    private Map<String, Object> findUpdateInfo(GiftCertificate certificate) {
        Map<String, Object> updateInfo = new HashMap<>();
        GiftCertificateValidator giftCertificateValidator =
                (GiftCertificateValidator) this.giftCertificateValidator;
        String name = certificate.getName();
        if (name != null) {
            if (!giftCertificateValidator.isNameValid(name)) {
                throw new InvalidEntityParameterException("certificate.name.invalid");
            }
            updateInfo.put("name", name);
        }
        String description = certificate.getDescription();
        if (description != null) {
            if (!giftCertificateValidator.isDescriptionValid(description)) {
                throw new InvalidEntityParameterException("certificate.description.invalid");
            }
            updateInfo.put("description", description);
        }
        BigDecimal price = certificate.getPrice();
        if (price != null) {
            if (!giftCertificateValidator.isPriceValid(price)) {
                throw new InvalidEntityParameterException("certificate.price.invalid");
            }
            updateInfo.put("price", price);
        }
        int duration = certificate.getDuration();
        if (duration != 0) {
            if (!giftCertificateValidator.isDurationValid(duration)) {
                throw new InvalidEntityParameterException("certificate.duration.invalid");
            }
            updateInfo.put("duration", duration);
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
    public List<GiftCertificateDto> findByParameters(String tagName, String partValue, SortContext sortContext) {
        if (isSortContextPassed(sortContext)) {
            validateSortContext(sortContext);
        }
        List<GiftCertificateDto> certificates = new ArrayList<>();
        giftCertificateRepository.findByParameters(tagName, partValue, sortContext).forEach(giftCertificate ->
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
            throw new InvalidEntityParameterException("certificate.invalid");
        }
    }

    private void validateTags(Set<TagDto> tags) {
        if (!tags.stream().allMatch(tagValidator::isValid)) {
            throw new InvalidEntityParameterException("tag.invalid");
        }
    }

    private void validateSortContext(SortContext context) {
        if (!sortContextValidator.isValid(context)) {
            throw new InvalidSortParameterException("sort.context.invalid");
        }
    }
}
