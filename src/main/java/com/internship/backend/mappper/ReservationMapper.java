package com.internship.backend.mappper;

import com.internship.backend.dto.ReservationDTO;
import com.internship.backend.exceptions.TennisCourtDoesNotExistsException;
import com.internship.backend.exceptions.UserDoesNotExistException;
import com.internship.backend.model.Reservation;
import com.internship.backend.model.TennisCourt;
import com.internship.backend.model.User;
import com.internship.backend.repository.TennisCourtRepository;
import com.internship.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TennisCourtRepository tennisCourtRepository;

    public Reservation reservatonMapper(ReservationDTO reservationDTO) throws UserDoesNotExistException, TennisCourtDoesNotExistsException {
        Reservation reservation = new Reservation();
        reservation.setStartTime(reservationDTO.getStartTime());
        reservation.setEndTime(reservationDTO.getEndTime());

        User user = userRepository.findById(reservationDTO.getUserId()).orElseThrow(()-> new UserDoesNotExistException("User not found"));
        reservation.setUser(user);

        TennisCourt tennisCourt = tennisCourtRepository.findById(reservationDTO.getTennisCourtId()).orElseThrow(()-> new TennisCourtDoesNotExistsException("TennisCourt not found"));
        reservation.setTennisCourt(tennisCourt);

        return reservation;
    }
}
