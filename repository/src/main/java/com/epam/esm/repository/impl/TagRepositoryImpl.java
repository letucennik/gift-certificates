package com.epam.esm.repository.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DAOException;
import com.epam.esm.repository.TagRepository;
import org.hibernate.JDBCException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

@Repository
public class TagRepositoryImpl implements TagRepository {

    //    private static final String CREATE_TAG = "INSERT INTO tag (name) VALUES (?)";
//    private static final String SELECT_TAG_BY_ID = "SELECT id, name FROM tag WHERE id=?";
//    private static final String SELECT_TAG_BY_NAME = "SELECT id, name FROM tag WHERE name=?";
//    private static final String DELETE_TAG = "DELETE FROM tag WHERE id=?";
//
//    private final JdbcTemplate jdbcTemplate;
//    private final RowMapper<Tag> tagMapper;
//
//    @Autowired
//    public TagRepositoryImpl(JdbcTemplate jdbcTemplate, RowMapper<Tag> tagMapper) {
//        this.jdbcTemplate = jdbcTemplate;
//        this.tagMapper = tagMapper;
//    }
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public long create(Tag tag) {
        try {
            entityManager.persist(tag);
        } catch (PersistenceException e) {
            throw new DAOException(e);
        }
        return tag.getId();
    }

    @Override
    public Optional<Tag> read(long id) {
        return Optional.ofNullable(entityManager.find(Tag.class, id));
    }

    @Override
    public void delete(long id) {
        Tag tag = entityManager.find(Tag.class, id);
        try {
            entityManager.remove(tag);
        } catch (IllegalArgumentException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public Optional<Tag> findByName(String name) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        criteriaQuery.where(criteriaBuilder.equal(root.get("name"), name));
        return entityManager.createQuery(criteriaQuery).getResultList().stream().findAny();
    }
}
