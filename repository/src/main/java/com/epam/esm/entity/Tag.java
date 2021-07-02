package com.epam.esm.entity;

import com.epam.esm.entity.audit.AuditListener;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@NoArgsConstructor
@Data
@EntityListeners(AuditListener.class)
@Table(name = "tag")
public class Tag implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @Column
    private String name;

    @OneToMany(mappedBy = "tag")
    private Set<CertificateTag> certificateTags;

    public Tag(String name) {
        this.name = name;
    }

    public Tag(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Tag(String name, Set<CertificateTag> certificateTags) {
        this.name = name;
        this.certificateTags = certificateTags;
    }

    public Set<CertificateTag> getCertificateTags() {
        return certificateTags;
    }
}
