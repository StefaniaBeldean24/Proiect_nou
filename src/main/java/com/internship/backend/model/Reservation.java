package com.internship.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Setter
@Getter
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users user;
    
    @ManyToOne
    @JoinColumn(name = "tennis_court_id")
    private TennisCourt tennisCourt;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "minute", column = @Column(name = "start_minute")),
            @AttributeOverride(name = "hour", column = @Column(name = "start_hour")),
            @AttributeOverride(name = "day", column = @Column(name = "start_day")),
            @AttributeOverride(name = "month", column = @Column(name = "start_month")),
            @AttributeOverride(name = "year", column = @Column(name = "start_year"))
    })
    private NewDate startTime;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "minute", column = @Column(name = "end_minute")),
            @AttributeOverride(name = "hour", column = @Column(name = "end_hour")),
            @AttributeOverride(name = "day", column = @Column(name = "end_day")),
            @AttributeOverride(name = "month", column = @Column(name = "end_month")),
            @AttributeOverride(name = "year", column = @Column(name = "end_year"))
    })
    private NewDate endTime;
}
