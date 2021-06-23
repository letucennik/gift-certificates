package com.epam.esm.repository.impl;

import com.epam.esm.repository.config.TestJdbcConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestJdbcConfig.class})
class CertificateTagRepositoryImplTest {

    private static final long CERTIFICATE_ID_NON_EXIST = 34;
    private static final long TAG_ID_NON_EXIST = 23;

    private static final long CERTIFICATE_EXIST = 3;
    private static final long TAG_EXIST = 1;

    @Autowired
    private CertificateTagRepositoryImpl certificateTagRepository;

    @Test
    void testShouldCreateThrowsDataIntegrityViolationException() {
        assertThrows(DataIntegrityViolationException.class, () -> certificateTagRepository.create(CERTIFICATE_ID_NON_EXIST, TAG_ID_NON_EXIST));
    }

    @Test
    void testShouldFindTagsByCertificateId() {
        assertEquals(Collections.singletonList(TAG_EXIST), certificateTagRepository.findTagsIdByCertificateId(CERTIFICATE_EXIST));
    }
}