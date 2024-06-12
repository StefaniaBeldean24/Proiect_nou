package com.internship.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {
    private int id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

//    private int year;
//    private int month;
//    private int day;
//    private int startHour;
//    private int endHour;
//    private int userId;
//    private int tennisCourtId;
}
