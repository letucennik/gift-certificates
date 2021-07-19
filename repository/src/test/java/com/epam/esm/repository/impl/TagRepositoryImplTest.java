package com.epam.esm.repository.impl;

import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.repository.exception.DAOException;
import com.epam.esm.repository.config.TestJdbcConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestJdbcConfig.class})
@Transactional
class TagRepositoryImplTest {

    private Tag tagToCreate;
    private Tag firstTag;
    private Tag secondTag;

    @Autowired
    private TagRepository tagRepository;

    @BeforeEach
    void init() {
        tagToCreate = new Tag("name");
        firstTag = new Tag(1, "tag 1");
        secondTag = new Tag(2, "tag 2");
    }

    @Test
    void testShouldCreate() {
        Tag tag = tagRepository.save(tagToCreate);
        assertNotNull(tag);
    }

    @Test
    void testShouldFindById() {
        Optional<Tag> tag = tagRepository.findById(1L);
        assertTrue(tag.isPresent());
        assertEquals(tag.get(), firstTag);
    }

    @Test
    void testShouldFindByIdAndReturnEmpty() {
        Optional<Tag> tag = tagRepository.findById(45L);
        assertFalse(tag.isPresent());
    }

    @Test
    void testShouldDelete() {
        tagRepository.delete(secondTag);
        assertFalse(tagRepository.findById(secondTag.getId()).isPresent());
    }

    @Test
    void testShouldFindByName(){
        Optional<Tag> tag = tagRepository.findByName("tag 1");
        assertTrue(tag.isPresent());
        assertEquals(tag.get(), firstTag);
    }
}