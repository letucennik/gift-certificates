package com.epam.esm.entity;

import com.epam.esm.entity.audit.AuditListener;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@Data
@EntityListeners(AuditListener.class)
@Table(name = "tag")
@SqlResultSetMapping(name = "mostWidelyUsedTagMapper",
        classes = {
                @ConstructorResult(targetClass = MostWidelyUsedTag.class,
                        columns = {
                                @ColumnResult(name = "tag_id", type = Long.class),
                                @ColumnResult(name = "tag_name", type = String.class),
                                @ColumnResult(name = "highest_cost", type = BigDecimal.class)
                        })})
public class Tag implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    public Tag(String name) {
        this.name = name;
    }

    public Tag(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
