package com.epam.esm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tag implements Serializable {

    private long id;
    private String name;

    public Tag(String name) {
        this.name = name;
    }
}
