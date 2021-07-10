package com.epam.esm.entity;

import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private LocalDateTime date;

    @Column
    private BigDecimal cost;

    @Column(name = "user_id")
    private long userId;

    @ManyToMany()
    @JoinTable(
            name = "order_certificates",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "certificate_id")
    )
    @Fetch(FetchMode.SUBSELECT)
    private List<GiftCertificate> certificates;
}
