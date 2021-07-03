package com.epam.esm.repository.impl;

import com.epam.esm.entity.CertificateTag;
import com.epam.esm.exception.DAOException;
import com.epam.esm.repository.config.TestJdbcConfig;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestJdbcConfig.class})
@Transactional
class CertificateTagRepositoryImplTest {

    private static final long CERTIFICATE_ID_NON_EXIST = 34;
    private static final long TAG_ID_NON_EXIST = 23;

    private static final long CERTIFICATE_EXIST = 3;
    private static final long TAG_EXIST = 2;
    private static final long TAG_EXIST_1 = 3;

    @Autowired
    private CertificateTagRepositoryImpl certificateTagRepository;

    @Test
    void testShouldCreate() {
        CertificateTag certificateTag = certificateTagRepository.create(CERTIFICATE_EXIST, TAG_EXIST);
        assertNotNull(certificateTag);
    }

    @Test
    void testShouldCreateThrowsDAOException() {
        assertThrows(DAOException.class, () -> certificateTagRepository.create(CERTIFICATE_ID_NON_EXIST, TAG_ID_NON_EXIST));
    }

    @Test
    void testShouldFindTagsByCertificateId() {
        assertEquals(Arrays.asList(TAG_EXIST, TAG_EXIST_1), certificateTagRepository.findTagsIdByCertificateId(CERTIFICATE_EXIST));
    }
}