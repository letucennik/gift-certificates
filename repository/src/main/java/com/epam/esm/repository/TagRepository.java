package com.epam.esm.repository;

import com.epam.esm.repository.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    String GET_MOST_WIDELY_USED_TAG = "SELECT tag_id AS id, tag_name AS name FROM " +
            " (SELECT tag.id AS tag_id, tag.name AS tag_name, COUNT(tag_id) AS tag_count FROM tag " +
            " JOIN m2m_certificates_tags ON tag.id = m2m_certificates_tags.tag_id " +
            " JOIN gift_certificate ON m2m_certificates_tags.gift_certificate_id = gift_certificate.id " +
            " JOIN order_certificates ON gift_certificate.id = order_certificates.certificate_id " +
            " JOIN orders ON order_certificates.order_id = orders.id " +
            " JOIN (SELECT SUM(cost) AS orders_cost, user_id AS ui FROM orders GROUP BY user_id)" +
            " AS a ON orders.user_id = a.ui WHERE orders_cost = " +
            " (SELECT SUM(cost) AS orders_cost FROM orders GROUP BY user_id ORDER BY orders_cost DESC LIMIT 1)" +
            " GROUP BY tag_id ORDER BY tag_count DESC LIMIT 1) AS b";

    /**
     * Finds Tag by name.
     *
     * @param name Tag name to find
     * @return Optional of found tag
     */
    Optional<Tag> findByName(String name);

    /**
     * Finds the most widely used tag of a user with the highest cost of all orders
     *
     * @return found tag
     */
    @Query(value = GET_MOST_WIDELY_USED_TAG, nativeQuery = true)
    Tag getMostWidelyUsedTag();
}
