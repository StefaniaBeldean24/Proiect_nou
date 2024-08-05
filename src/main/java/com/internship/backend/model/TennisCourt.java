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
public class TennisCourt {

    @Id
    private String id;

    private String name;
    private String details;
    private Location location;
    private List<Price> prices;

    public TennisCourt() {
        this.id = UUID.randomUUID().toString();
    }
}