package com.internship.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;

import java.util.List;

@Document
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class TennisCourt {

    @Id
    private int id;

    private String name;
    private String details;

    private Location location;

    private List<Price> prices;


}