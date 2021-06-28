create table gift_certificate
(
    id               BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name             varchar(305),
    description      VARCHAR(2000),
    price            DECIMAL(10, 2),
    duration         INT,
    create_date      TIMESTAMP DEFAULT NOW(),
    last_update_date TIMESTAMP DEFAULT NOW()
);
create table tag
(
    id   BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(305)
);
create table m2m_certificates_tags
(
    `gift_certificate_id` INT UNSIGNED NOT NULL,
    `tag_id`              INT UNSIGNED NOT NULL,
    PRIMARY KEY (`gift_certificate_id`, `tag_id`),
    CONSTRAINT `gift_certificate`
        FOREIGN KEY (`gift_certificate_id`)
            REFERENCES `gift_certificate` (`id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    CONSTRAINT `tag`
        FOREIGN KEY (`tag_id`)
            REFERENCES `tag` (`id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE
)