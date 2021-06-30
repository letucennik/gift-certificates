package com.epam.esm.repository.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.config.TestJdbcConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = {TestJdbcConfig.class})
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestJdbcConfig.class})
@Transactional
class TagRepositoryImplTest {

    private Tag tagToCreate;
    private Tag firstTag;
    private Tag secondTag;

    @BeforeEach
    void init() {
        tagToCreate = new Tag("name");
        firstTag = new Tag("tag 1");
        secondTag = new Tag("tag 2");
    }

    @Autowired
    private TagRepositoryImpl tagRepository;

    @Test
    void testShouldCreate() {
        Long id = tagRepository.create(tagToCreate);
        assertNotNull(id);
    }

    @Test
    void testShouldFindById() {
        Optional<Tag> tag = tagRepository.read(firstTag.getId());
        assertTrue(tag.isPresent());
        assertEquals(tag.get(), firstTag);
    }

    @Test
    void testShouldFindByIdAndReturnEmpty() {
        Optional<Tag> tag = tagRepository.read(45);
        assertFalse(tag.isPresent());
    }

    @Test
    void testShouldDelete() {
        tagRepository.delete(secondTag.getId());
        assertFalse(tagRepository.read(secondTag.getId()).isPresent());
    }

    @Test
    void testShouldTryDeleteByIdNonExistingTag() {
//        assertEquals(0, tagRepository.delete(454));
    }
}