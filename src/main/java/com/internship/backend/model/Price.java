package com.internship.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;

@Document
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Price {

    @Id
    private Integer id;

    private String season;
    private String periodOfDay;
    private int price;

    private TennisCourt tennisCourt;
}