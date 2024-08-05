package com.internship.backend.model;

import com.internship.backend.dto.ReservationDTO;
import com.internship.backend.exceptions.InvalidDateException;

import java.time.LocalDateTime;

public class ReservationValidator {
    public static final String INVALID_DATE_ERROR = "Reservations can only be added for the current date or within the next 24 hours";
    public static final String DIFFERENT_DAY_ERROR = "The start date and the end date must be within the same day";
    public static final String INVALID_DURATION_ERROR = "The reservation duration must be exactly 2 hours";

    private ReservationDTO reservationDTO;

    public ReservationValidator(ReservationDTO reservationDTO) {
        this.reservationDTO = reservationDTO;
    }

    public void validate() throws InvalidDateException {
        LocalDateTime now = LocalDateTime.now();

        if (reservationDTO.getStartTime().isBefore(now) || reservationDTO.getStartTime().isAfter(now.plusHours(24))) {
            throw new InvalidDateException(INVALID_DATE_ERROR);
        }

        if (!reservationDTO.getStartTime().toLocalDate().equals(reservationDTO.getEndTime().toLocalDate())) {
            throw new InvalidDateException(DIFFERENT_DAY_ERROR);
        }

        if (reservationDTO.getEndTime().getHour() - reservationDTO.getStartTime().getHour() != 2) {
            throw new InvalidDateException(INVALID_DURATION_ERROR);
        }
    }
}
