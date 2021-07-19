package com.epam.esm.repository;

import com.epam.esm.repository.entity.GiftCertificate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GiftCertificateRepository extends JpaRepository<GiftCertificate,Long>, GiftCertificateRepositoryCustom {

}
