package com.internship.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;


@Document
@Setter
@Getter
@Builder
@AllArgsConstructor
public class Reservation {

    @Id
    private String id;
    private String userUsername;
    private String tennisCourtName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Reservation() {
        this.id = UUID.randomUUID().toString();
    }
}