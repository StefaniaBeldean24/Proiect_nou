package com.internship.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;

import java.util.UUID;

@Document
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Authority {

    @Id
    private String id;

    private String name;

    public Authority(String name) {
        this.name = name;
    }

    public Authority() {
        this.id = UUID.randomUUID().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Authority authority = (Authority) o;
        return name.equals(authority.name);
    }
}