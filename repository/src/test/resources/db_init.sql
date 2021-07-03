INSERT INTO gift_certificate (name, description, price, duration, create_date, last_update_date)
VALUES ('certificate 1', 'description 1', 1.1, 1, '2020-01-01 01:11:11', '2021-01-01 01:22:11'),
       ('certificate 2', 'description 2', 2.2,2, '2020-02-02 02:22:22', '2021-02-02 02:33:22'),
       ('certificate 3', 'description 3', 3.3,3, '2020-03-03 03:33:33', '2021-03-03 03:44:33');

INSERT INTO tag (name) VALUES ('tag 1'), ('tag 2'), ('tag 3'), ('tag 4');

INSERT INTO `m2m_certificates_tags` (`gift_certificate_id`,`tag_id`) VALUES (1,1);
INSERT INTO `m2m_certificates_tags` (`gift_certificate_id`,`tag_id`) VALUES (1,2);
INSERT INTO `m2m_certificates_tags` (`gift_certificate_id`,`tag_id`) VALUES (2,1);
INSERT INTO `m2m_certificates_tags` (`gift_certificate_id`,`tag_id`) VALUES (2,2);
INSERT INTO `m2m_certificates_tags` (`gift_certificate_id`,`tag_id`) VALUES (3,2);
INSERT INTO `m2m_certificates_tags` (`gift_certificate_id`,`tag_id`) VALUES (3,3);