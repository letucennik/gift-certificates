package com.epam.esm.repository.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.config.TestJdbcConfig;
import com.epam.esm.repository.query.SortContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Component
@ContextConfiguration(classes = {TestJdbcConfig.class})
@ComponentScan("com.epam.esm.entity.mapper")
class GiftCertificateRepositoryImplTest {

    private static final String TAG_NAME = "tag 1";

    private GiftCertificate certificateToCreate;
    private GiftCertificate firstCertificate;
    private GiftCertificate secondCertificate;
    private GiftCertificate thirdCertificate;

    private List<GiftCertificate> sortedAsc;

    @Autowired
    private GiftCertificateRepository giftCertificateRepository;

    @BeforeEach
    void init() {
        certificateToCreate = new GiftCertificate(
                4L, "certificate new", "description new", new BigDecimal("1.10"),
                LocalDateTime.parse("2020-01-01T01:11:11"),
                LocalDateTime.parse("2021-01-01T01:22:11"), 1);
        firstCertificate = new GiftCertificate(
                1L, "certificate 1", "description 1", new BigDecimal("1.10"),
                LocalDateTime.parse("2020-01-01T01:11:11"),
                LocalDateTime.parse("2021-01-01T01:22:11"), 1);
        secondCertificate = new GiftCertificate(
                2L, "certificate 2", "description 2", new BigDecimal("2.20"),
                LocalDateTime.parse("2020-02-02T02:22:22"),
                LocalDateTime.parse("2021-02-02T02:33:22"), 2);
        thirdCertificate = new GiftCertificate(3L, "certificate 3", "description 3", new BigDecimal("3.30"),
                LocalDateTime.parse("2020-03-03T03:33:33"),
                LocalDateTime.parse("2021-03-03T03:44:33"), 3);
        sortedAsc = Arrays.asList(firstCertificate, thirdCertificate, certificateToCreate);
    }

    @Test
    void testShouldFindByParametersValue() {
        List<GiftCertificate> giftCertificates = giftCertificateRepository.findByParameters(null, "certificate", null);
        assertTrue(giftCertificates.containsAll(sortedAsc));
    }

    @Test
    void testShouldByParametersSort() {
        List<GiftCertificate> giftCertificates = giftCertificateRepository.findByParameters(null, "desc", new SortContext(Collections.singletonList("name"), Collections.singletonList(SortContext.OrderType.ASC)));
        assertEquals(sortedAsc, giftCertificates);
    }

    @Test
    void testShouldCreate() {
        Long id = giftCertificateRepository.create(certificateToCreate);
        assertNotNull(id);
    }

    @Test
    void testShouldFindById() {
        Optional<GiftCertificate> giftCertificate = giftCertificateRepository.read(1);
        assertTrue(giftCertificate.isPresent() && giftCertificate.get().getId() == firstCertificate.getId());
    }

    @Test
    void testShouldFindByIdAndReturnEmpty() {
        Optional<GiftCertificate> giftCertificate = giftCertificateRepository.read(122);
        assertFalse(giftCertificate.isPresent());
    }

    @Test
    void testShouldUpdateById() {
        Map<String, Object> updateInfo = new HashMap<>();
        updateInfo.put("name", "certificate new name");
        giftCertificateRepository.update(firstCertificate.getId(), updateInfo);
        Optional<GiftCertificate> certificate = giftCertificateRepository.read(firstCertificate.getId());
        assertTrue(certificate.isPresent() && certificate.get().getName().equals("certificate new name"));
    }

    @Test
    void testShouldDeleteById() {
        giftCertificateRepository.delete(secondCertificate.getId());
        assertFalse(giftCertificateRepository.read(secondCertificate.getId()).isPresent());
    }

    @Test
    void testShouldTryDeleteByIdNonExistingCertificate() {
        assertEquals(0, giftCertificateRepository.delete(345));
    }


    @Test
    void testShouldFindByParametersNameValue() {
        List<GiftCertificate> giftCertificates = giftCertificateRepository.findByParameters("tag 1", "1", null);
        assertEquals(Collections.singletonList(firstCertificate), giftCertificates);
    }


}