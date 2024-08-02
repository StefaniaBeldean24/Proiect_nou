package com.internship.backend.model;

import com.internship.backend.exceptions.InvalidDateException;

import java.time.LocalDateTime;

public class ReservationValidator {

    public static final String INVALID_DATE_ERROR = "Reservations can only be added for the current date or within the next 24 hours";
    public static final String DIFFERENT_DAY_ERROR = "The start date and the end date must be within the same day";
    public static final String INVALID_DURATION_ERROR = "The reservation duration must be exactly 2 hours";

    private Reservation reservation;

    public ReservationValidator(Reservation reservation) {
        this.reservation = reservation;
    }

    public void validate() throws InvalidDateException {
        LocalDateTime now = LocalDateTime.now();

        if (reservation.getStartTime().isBefore(now) || reservation.getStartTime().isAfter(now.plusHours(24))) {
            throw new InvalidDateException(INVALID_DATE_ERROR);
        }

        if (!reservation.getStartTime().toLocalDate().equals(reservation.getEndTime().toLocalDate())) {
            throw new InvalidDateException(DIFFERENT_DAY_ERROR);
        }

        if (reservation.getEndTime().getHour() - reservation.getStartTime().getHour() != 2) {
            throw new InvalidDateException(INVALID_DURATION_ERROR);
        }
    }
}
