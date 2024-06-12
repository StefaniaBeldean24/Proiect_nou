package com.internship.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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

    private LocalDateTime startTime;
    private LocalDateTime endTime;

//    private int year;
//    private int month;
//    private int day;
//    private int startHour;
//    private int endHour;
}

//endpoint in care user-ul sa vada toate terenurile disponibile dintr-o anumita data
//