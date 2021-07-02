package com.epam.esm.repository.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.DAOException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.config.TestJdbcConfig;
import com.epam.esm.repository.query.SortContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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

    @Autowired
    private GiftCertificateRepository giftCertificateRepository;

    @BeforeEach
    void init() {
        certificateToCreate = new GiftCertificate(
                "certificate new", "description new", new BigDecimal("1.10"),
                1, LocalDateTime.parse("2020-01-01T01:11:11"),
                LocalDateTime.parse("2021-01-01T01:22:11"));
        firstCertificate = new GiftCertificate(
                "certificate 1", "description 1", new BigDecimal("1.10"),
                1, LocalDateTime.parse("2020-01-01T01:11:11"),
                LocalDateTime.parse("2021-01-01T01:22:11"));
        firstCertificate.setId(1);
        secondCertificate = new GiftCertificate("certificate 2", "description 2", new BigDecimal("2.20"),
                2, LocalDateTime.parse("2020-02-02T02:22:22"),
                LocalDateTime.parse("2021-02-02T02:33:22"));
        secondCertificate.setId(2);
        thirdCertificate = new GiftCertificate("certificate 3", "description 3", new BigDecimal("3.30"),
                3, LocalDateTime.parse("2020-03-03T03:33:33"),
                LocalDateTime.parse("2021-03-03T03:44:33"));
        thirdCertificate.setId(3);
        sortedAsc = Arrays.asList(firstCertificate, secondCertificate, thirdCertificate);
    }

    @Test
    void testShouldFindByParametersNameValue() {
        List<GiftCertificate> giftCertificates = giftCertificateRepository.findByParameters("tag 1", "1", null);
        assertEquals(Collections.singletonList(firstCertificate), giftCertificates);
    }

    @Test
    void testShouldFindByParametersValue() {
        List<GiftCertificate> giftCertificates = giftCertificateRepository.findByParameters(null, "description", null);
        assertEquals(sortedAsc, giftCertificates);
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
        assertTrue(giftCertificate.isPresent());
        assertEquals(1, giftCertificate.get().getId());
    }

    @Test
    void testShouldFindByIdAndReturnEmpty() {
        Optional<GiftCertificate> giftCertificate = giftCertificateRepository.read(122);
        assertFalse(giftCertificate.isPresent());
    }

    @Test
    void testShouldUpdateById() {
        firstCertificate.setName("new name");
        GiftCertificate updated = giftCertificateRepository.update(firstCertificate);
        assertEquals(updated.getName(), "new name");
    }

    @Test
    void testShouldDeleteById() {
        giftCertificateRepository.delete(2);
        assertFalse(giftCertificateRepository.read(secondCertificate.getId()).isPresent());
    }

    @Test
    void testShouldTryDeleteByIdNonExistingCertificate() {
        assertThrows(DAOException.class, () -> {
            giftCertificateRepository.delete(566);
        });
    }

}