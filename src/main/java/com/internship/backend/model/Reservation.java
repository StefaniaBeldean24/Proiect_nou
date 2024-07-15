package com.internship.backend.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;


@Document
@Setter
@Getter
public class Reservation {

    @Id
    private Integer id;

    @Field
    private Integer userId;

    @Field
    private Integer tennisCourtId;

    @Field
    private NewDate startTime;

    @Field
    private NewDate endTime;
}