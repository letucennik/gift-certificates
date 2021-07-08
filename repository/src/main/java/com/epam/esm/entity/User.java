package com.epam.esm.entity;

import com.epam.esm.entity.audit.AuditListener;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@EntityListeners(AuditListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    public User(String name) {
        this.name = name;
    }
}
