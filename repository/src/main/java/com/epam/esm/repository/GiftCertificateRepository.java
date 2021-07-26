package com.epam.esm.repository;

import com.epam.esm.repository.entity.GiftCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GiftCertificateRepository extends JpaSpecificationExecutor<GiftCertificate>, JpaRepository<GiftCertificate, Long> {


}
