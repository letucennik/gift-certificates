package com.epam.esm.entity;

import com.epam.esm.entity.audit.AuditListener;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = "certificateTags")
@EntityListeners(AuditListener.class)
@Table(name = "gift_certificate")
public class GiftCertificate implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private BigDecimal price;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;

    @Column
    private int duration;

    @OneToMany(mappedBy = "certificate")
    private Set<CertificateTag> certificateTags=new HashSet<>();

    public GiftCertificate(String name, String description, BigDecimal price, int duration, LocalDateTime createDate, LocalDateTime lastUpdateDate) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
    }

    public GiftCertificate(String name, String description, BigDecimal price, LocalDateTime createDate, LocalDateTime lastUpdateDate, int duration, Set<CertificateTag> certificateTags) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.duration = duration;
        this.certificateTags = certificateTags;
    }

    public GiftCertificate(String name, String description, BigDecimal price, int duration, Set<CertificateTag> certificateTags) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.certificateTags = certificateTags;
    }

    public GiftCertificate(long id, String name, String description, BigDecimal price, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
    }

    public Set<CertificateTag> getCertificateTags() {
        return certificateTags;
    }

}
