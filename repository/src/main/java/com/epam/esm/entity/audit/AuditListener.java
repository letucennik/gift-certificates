package com.epam.esm.entity.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class AuditListener {

    private static final String PRE_INSERT = "Pre insert: ";
    private static final String POST_INSERT = "Post insert: ";
    private static final String PRE_UPDATE = "Pre update: ";
    private static final String POST_UPDATE = "Post update: ";
    private static final String PRE_REMOVE = "Pre remove: ";
    private static final String POST_REMOVE = "Post remove: ";

    private final Logger logger = LoggerFactory.getLogger(AuditListener.class);

    @PrePersist
    private void prePersistAudit(Object object) {
        logger.info(PRE_INSERT + object);
    }

    @PostPersist
    public void onPostPersist(Object object) {
        logger.info(POST_INSERT + object);
    }

    @PreUpdate
    public void onPreUpdate(Object object) {
        logger.info(PRE_UPDATE + object);

    }

    @PostUpdate
    public void onPostUpdate(Object object) {
        logger.info(POST_UPDATE + object);

    }

    @PreRemove
    public void onPreRemove(Object object) {
        logger.info(PRE_REMOVE + object);

    }

    @PostRemove
    public void onPostRemove(Object object) {
        logger.info(POST_REMOVE + object);
    }

}
