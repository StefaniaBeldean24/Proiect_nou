package com.internship.backend.mappper;

import com.internship.backend.dto.ReservationDTO;
import com.internship.backend.model.Reservation;
import com.internship.backend.model.TennisCourt;
import com.internship.backend.model.User;

public class ReservationMapper {

    public Reservation mapToReservation(ReservationDTO reservationDTO, User user, TennisCourt tennisCourt) {
        Reservation reservation = new Reservation();
        reservation.setStartTime(reservationDTO.getStartTime());
        reservation.setEndTime(reservationDTO.getEndTime());
        reservation.setUser(user);
        reservation.setTennisCourt(tennisCourt);
        return reservation;
    }
}
