package com.epam.esm.entity;

import com.epam.esm.entity.audit.AuditListener;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @Column(name="create_date")
    private LocalDateTime createDate;

    @Column(name="last_update_date")
    private LocalDateTime lastUpdateDate;

    @Column
    private int duration;

    public GiftCertificate(long id, String name, String description, BigDecimal price, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
    }

    @OneToMany(mappedBy = "certificate")
    private Set<CertificateTag> certificateTags;
}
