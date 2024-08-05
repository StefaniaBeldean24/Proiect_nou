package com.internship.backend.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;

import java.util.UUID;

@Document
@Setter
@Getter
public class Price {

    @Id
    private String id;

    private String season;
    private String periodOfDay;
    private int price;

    private TennisCourt tennisCourt;

    public Price() {
        this.id = UUID.randomUUID().toString();
    }
}