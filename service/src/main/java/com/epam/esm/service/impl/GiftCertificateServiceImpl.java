package com.epam.esm.service.impl;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DuplicateEntityException;
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
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    public static final String CERTIFICATE_NOT_FOUND = "Certificate not found";

    private final GiftCertificateRepository giftCertificateRepository;
    private final TagRepository tagRepository;
    private final CertificateTagRepository certificateTagRepository;
    private final Validator<GiftCertificate> giftCertificateValidator;
    private final Validator<Tag> tagValidator;
    private final Validator<SortContext> sortContextValidator;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepository giftCertificateRepository, CertificateTagRepository certificateTagRepository,
                                      TagRepository tagRepository,
                                      Validator<GiftCertificate> giftCertificateValidator,
                                      Validator<Tag> tagValidator, Validator<SortContext> sortContextValidator) {
        this.giftCertificateRepository = giftCertificateRepository;
        this.certificateTagRepository = certificateTagRepository;
        this.tagRepository = tagRepository;
        this.giftCertificateValidator = giftCertificateValidator;
        this.tagValidator = tagValidator;
        this.sortContextValidator = sortContextValidator;
    }

    @Override
    @Transactional
    public GiftCertificateDto create(GiftCertificateDto giftCertificateDto) {
        GiftCertificate giftCertificate = getGiftCertificateFromDto(giftCertificateDto);
        List<Tag> tags = giftCertificateDto.getTags();
        validateGiftCertificate(giftCertificate);
        validateTags(tags);
        if (new HashSet<>(tags).size() < tags.size()) {
            throw new DuplicateEntityException("Trying to add 2 identical tags to certificate");
        }
        long certificateId = giftCertificateRepository.create(giftCertificate);
        for (Tag tag : giftCertificateDto.getTags()) {
            Optional<Tag> tagOptional = tagRepository.findByName(tag.getName());
            long tagId = tagOptional.map(Tag::getId).orElseGet(() -> tagRepository.create(tag));
            certificateTagRepository.create(certificateId, tagId);
        }
        giftCertificate.setId(certificateId);
        return createGiftCertificateDto(giftCertificate);
    }

    @Override
    public GiftCertificate read(long id) {
        Optional<GiftCertificate> giftCertificate = giftCertificateRepository.read(id);
        return giftCertificate.orElseThrow(() -> new NoSuchEntityException(CERTIFICATE_NOT_FOUND));
    }

    @Override
    @Transactional
    public GiftCertificateDto update(long id, GiftCertificateDto dto) {
        GiftCertificate giftCertificate = getGiftCertificateFromDto(dto);
        if (!giftCertificateRepository.read(id).isPresent()) {
            throw new NoSuchEntityException(CERTIFICATE_NOT_FOUND);
        }
        giftCertificateRepository.update(id, findUpdateInfo(giftCertificate));
        List<Tag> tags = dto.getTags();
        if (tags != null) {
            updateTags(id, tags);
        }
        return createGiftCertificateDto(giftCertificateRepository.read(id).get());
    }

    private Map<String, Object> findUpdateInfo(GiftCertificate certificate) {
        Map<String, Object> updateInfo = new HashMap<>();
        GiftCertificateValidator giftCertificateValidator =
                (GiftCertificateValidator) this.giftCertificateValidator;
        String name = certificate.getName();
        if (name != null) {
            if (!giftCertificateValidator.isNameValid(name)) {
                throw new InvalidEntityParameterException("Certificate name is invalid");
            }
            updateInfo.put("name", name);
        }
        String description = certificate.getDescription();
        if (description != null) {
            if (!giftCertificateValidator.isDescriptionValid(description)) {
                throw new InvalidEntityParameterException("Certificate description is invalid");
            }
            updateInfo.put("description", description);
        }
        BigDecimal price = certificate.getPrice();
        if (price != null) {
            if (!giftCertificateValidator.isPriceValid(price)) {
                throw new InvalidEntityParameterException("Certificate price is invalid");
            }
            updateInfo.put("price", price);
        }
        int duration = certificate.getDuration();
        if (duration != 0) {
            if (!giftCertificateValidator.isDurationValid(duration)) {
                throw new InvalidEntityParameterException("Certificate duration is invalid");
            }
            updateInfo.put("duration", duration);
        }
        return updateInfo;
    }

    private void updateTags(long certificateId, List<Tag> tags) {
        for (Tag tag : tags) {
            String tagName = tag.getName();
            Optional<Tag> tagOptional = tagRepository.findByName(tagName);
            Tag current = tagOptional.orElseGet(() -> createTag(tag));
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
        return giftCertificateRepository.findByParameters(tagName, partValue, sortContext)
                .stream()
                .map(this::createGiftCertificateDto)
                .collect(Collectors.toList());
    }

    private boolean isSortContextPassed(SortContext sortContext) {
        return sortContext != null && sortContext.getSortColumns() != null;
    }

    private GiftCertificateDto createGiftCertificateDto(GiftCertificate giftCertificate) {
        GiftCertificateDto giftCertificateDto = new GiftCertificateDto(giftCertificate);
        Set<Optional<Tag>> optionalTags = new HashSet<>();
        List<Long> tagsId = certificateTagRepository.findTagsIdByCertificateId(giftCertificate.getId());
        tagsId.forEach(id -> optionalTags.add(tagRepository.read(id)));
        optionalTags.forEach(tag -> giftCertificateDto.addTag(tag.get()));
        return giftCertificateDto;
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
            throw new InvalidEntityParameterException("Certificate is invalid");
        }
    }

    private void validateTags(List<Tag> tags) {
        if (!tags.stream().allMatch(tagValidator::isValid)) {
            throw new InvalidEntityParameterException("Tag is invalid");
        }
    }

    private void validateSortContext(SortContext context) {
        if (!sortContextValidator.isValid(context)) {
            throw new InvalidSortParameterException("Sort context is invalid");
        }
    }

    private GiftCertificate getGiftCertificateFromDto(GiftCertificateDto dto) {
        GiftCertificate certificate = new GiftCertificate(dto.getId(), dto.getName(), dto.getDescription(), dto.getPrice(), dto.getDuration());
        if (dto.getCreateDate() == null) {
            certificate.setCreateDate(LocalDateTime.now());
        } else {
            certificate.setCreateDate(LocalDateTime.parse(dto.getCreateDate()));
        }
        if (dto.getLastUpdateDate() == null) {
            certificate.setLastUpdateDate(LocalDateTime.now());
        } else {
            certificate.setLastUpdateDate(LocalDateTime.parse(dto.getLastUpdateDate()));
        }
        return certificate;
    }
}
