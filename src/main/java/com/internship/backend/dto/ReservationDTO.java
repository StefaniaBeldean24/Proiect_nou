package com.internship.backend.dto;

import com.internship.backend.model.NewDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {
    private int userId;
    private int tennisCourtId;
    private NewDate startTime;
    private NewDate endTime;
}
