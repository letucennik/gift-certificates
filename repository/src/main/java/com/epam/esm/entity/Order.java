package com.epam.esm.entity;

import com.epam.esm.entity.audit.AuditListener;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@EntityListeners(AuditListener.class)
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private LocalDateTime date;

    @Column
    private BigDecimal cost;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany()
    @JoinTable(
            name = "order_certificates",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "certificate_id")
    )
    @Fetch(FetchMode.SUBSELECT)
    private List<GiftCertificate> certificates;
}
