package com.internship.backend.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

@Document
@Getter
@Setter
public class Authority {

    @Id
    private Integer id;

    @Field
    private String name;

    public Authority(String roleUser) {
        this.name = roleUser;
    }

    public Authority() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Authority authority = (Authority) o;
        return name.equals(authority.name);
    }
}