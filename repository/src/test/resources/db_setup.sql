create table gift_certificate
(
    id               BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name             varchar(305),
    description      VARCHAR(2000),
    price            DECIMAL(10, 2),
    duration         BIGINT,
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
);
CREATE TABLE user
(
    id       INT PRIMARY KEY AUTO_INCREMENT,
    name    VARCHAR(305) UNIQUE
);
CREATE TABLE orders
(
    id     BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    cost    DECIMAL(11, 2),
    date    TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user (id)
);
CREATE TABLE order_certificates
(
    order_id       INT,
    certificate_id INT,
    PRIMARY KEY (order_id, certificate_id),
    FOREIGN KEY (order_id) REFERENCES orders (id),
    FOREIGN KEY (certificate_id) REFERENCES gift_certificate (id)
)