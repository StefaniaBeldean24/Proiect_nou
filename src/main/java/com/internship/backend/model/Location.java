package com.internship.backend.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Document
@Setter
@Getter
@AllArgsConstructor
public class Location {

    @Id
    private String id;

    private String name;
    private String details;
    private List<TennisCourt> tennisCourt;

    public Location() {
        this.id = UUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", details='" + details + '\'' +
                ", tennisCourt=" + tennisCourt +
                '}';
    }
}