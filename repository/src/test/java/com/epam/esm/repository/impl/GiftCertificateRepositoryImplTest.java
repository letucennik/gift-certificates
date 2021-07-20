package com.epam.esm.repository.impl;

import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.config.TestJdbcConfig;
import com.epam.esm.repository.entity.GiftCertificate;
import com.epam.esm.repository.query.SortContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestJdbcConfig.class})
@Transactional
class GiftCertificateRepositoryImplTest {

    private static final String TAG_NAME = "tag 1";

    private GiftCertificate certificateToCreate;
    private GiftCertificate firstCertificate;
    private GiftCertificate secondCertificate;
    private GiftCertificate thirdCertificate;

    private List<GiftCertificate> sortedAsc;

    private static final Pageable DEFAULT_PAGEABLE = PageRequest.of(0, 25);

    @Autowired
    private GiftCertificateRepository giftCertificateRepository;


    @BeforeEach
    void init() {
        certificateToCreate = new GiftCertificate(
                "certificate new", "description new", new BigDecimal("1.10"),
                Duration.ofMillis(2), LocalDateTime.parse("2020-01-01T01:11:11"),
                LocalDateTime.parse("2021-01-01T01:22:11"));
        firstCertificate = new GiftCertificate(
                "certificate 1", "description 1", new BigDecimal("1.10"),
                Duration.ofMillis(1), LocalDateTime.parse("2020-01-01T01:11:11"),
                LocalDateTime.parse("2021-01-01T01:22:11"));
        firstCertificate.setId(1);
        secondCertificate = new GiftCertificate("certificate 2", "description 2", new BigDecimal("2.20"),
                Duration.ofMillis(2), LocalDateTime.parse("2020-02-02T02:22:22"),
                LocalDateTime.parse("2021-02-02T02:33:22"));
        secondCertificate.setId(2);
        thirdCertificate = new GiftCertificate("certificate 3", "description 3", new BigDecimal("3.30"),
                Duration.ofMillis(3), LocalDateTime.parse("2020-03-03T03:33:33"),
                LocalDateTime.parse("2021-03-03T03:44:33"));
        thirdCertificate.setId(3);
        sortedAsc = Arrays.asList(firstCertificate, secondCertificate, thirdCertificate);
    }

    @Test
    void testShouldFindByParametersValue() {
        List<GiftCertificate> giftCertificates = giftCertificateRepository.findAll(GiftCertificateRepository.findByParameters(null, "description", null, DEFAULT_PAGEABLE));
        assertTrue(sortedAsc.containsAll(giftCertificates));
    }

    @Test
    void testShouldByParametersSort() {
        List<GiftCertificate> giftCertificates = giftCertificateRepository.findAll(GiftCertificateRepository.findByParameters(null, "desc", new SortContext(Collections.singletonList("name"), Collections.singletonList(SortContext.OrderType.ASC)), DEFAULT_PAGEABLE));
        assertEquals(sortedAsc, giftCertificates);
    }

    @Test
    void testShouldCreate() {
        GiftCertificate certificate = giftCertificateRepository.save(certificateToCreate);
        assertNotNull(certificate);
    }

    @Test
    void testShouldFindById() {
        Optional<GiftCertificate> giftCertificate = giftCertificateRepository.findById(1L);
        assertTrue(giftCertificate.isPresent());
        assertEquals(1, giftCertificate.get().getId());
    }

    @Test
    void testShouldFindByIdAndReturnEmpty() {
        Optional<GiftCertificate> giftCertificate = giftCertificateRepository.findById(122L);
        assertFalse(giftCertificate.isPresent());
    }

    @Test
    void testShouldUpdateById() {
        firstCertificate.setName("new name");
        GiftCertificate updated = giftCertificateRepository.save(firstCertificate);
        assertEquals(updated.getName(), "new name");
    }

    @Test
    void testShouldDeleteById() {
        giftCertificateRepository.delete(giftCertificateRepository.findById(2L).get());
        assertFalse(giftCertificateRepository.findById(secondCertificate.getId()).isPresent());
    }


}