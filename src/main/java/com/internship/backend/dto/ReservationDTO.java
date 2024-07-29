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
    private int userId;
    private int tennisCourtId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
