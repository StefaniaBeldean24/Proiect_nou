package com.internship.backend.service;

import com.internship.backend.dto.ReservationDTO;
import com.internship.backend.exceptions.*;
import com.internship.backend.model.Reservation;
import com.internship.backend.model.ReservationValidator;
import com.internship.backend.model.TennisCourt;
import com.internship.backend.repository.ReservationRepository;
import com.internship.backend.repository.TennisCourtRepository;
import com.internship.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TennisCourtRepository tennisCourtRepository;

    @Autowired
    private UserRepository userRepository;

    public Reservation addReservation(ReservationDTO reservationDTO) throws ReservationAlreadyExists, InvalidDateException, UserDoesNotExistException, TennisCourtDoesNotExistsException {
        var validator = new ReservationValidator(reservationDTO);
        validator.validate();

        if (isValidReservation((reservationDTO))) {
            return addReservation(addIdTennisCourtUSerStartTimeEndTimeToReservation(reservationDTO));
        } else {
            throw new ReservationAlreadyExists("Reservation intersects another one");
        }
    }

    private Reservation addIdTennisCourtUSerStartTimeEndTimeToReservation(ReservationDTO reservationDTO) throws UserDoesNotExistException, TennisCourtDoesNotExistsException {


        var user = userRepository.findByUsername(reservationDTO.getUserUsername())
                .orElseThrow(() -> new UserDoesNotExistException("User not found"));

        var tennisCourt = tennisCourtRepository.findByName(reservationDTO.getTennisCourtName())
                .orElseThrow(() -> new TennisCourtDoesNotExistsException("Tennis Court not found"));

        var reservation = new Reservation();
        reservation.setId(UUID.randomUUID().toString());
        reservation.setTennisCourtName(tennisCourt.getName());
        reservation.setUserUsername(user.getUsername());
        reservation.setStartTime(reservationDTO.getStartTime());
        reservation.setEndTime(reservationDTO.getEndTime());

        return reservation;
    }

    public Reservation addReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    private boolean isValidReservation(ReservationDTO reservationDTO) {
        if (Objects.isNull(reservationDTO)) {
            return false;
        }

        var startTime = reservationDTO.getStartTime();
        var endTime = reservationDTO.getEndTime();

        if (Objects.isNull(startTime) || Objects.isNull(endTime)) {
            return false;
        }

        if (!startTime.isBefore(endTime)) {
            return false;
        }

        // Check for overlapping reservations
        boolean overlaps = reservationRepository.findAll().stream()
                .anyMatch(reservation ->
                        reservation.getTennisCourtName().equals(reservationDTO.getTennisCourtName()) &&
                                ((reservation.getStartTime().isBefore(endTime) && reservation.getEndTime().isAfter(startTime)) ||
                                        (reservation.getStartTime().isBefore(startTime) && reservation.getEndTime().isAfter(endTime)) ||
                                        (reservation.getStartTime().equals(startTime) && reservation.getEndTime().equals(endTime))));

        return !overlaps;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Optional<Reservation> getReservationById(String id) {
        return reservationRepository.findById(id);
    }

    public Reservation updateReservation(Reservation reservationDetails) throws ReservationDoesNotExistException {
        return reservationRepository.findById(reservationDetails.getId())
                .map(reservation -> updateOldReservation(reservation, reservationDetails))
                .orElseThrow(() -> new ReservationDoesNotExistException("Reservation not found"));
    }

    private Reservation updateOldReservation(Reservation reservation, Reservation reservationDetails) {
        reservation.setUserUsername(reservationDetails.getUserUsername());
        reservation.setTennisCourtName(reservationDetails.getTennisCourtName());
        reservation.setStartTime(reservationDetails.getStartTime());
        reservation.setEndTime(reservationDetails.getEndTime());
        return reservationRepository.save(reservation);
    }

    public void deleteReservation(String id) throws ReservationDoesNotExistException {
        if (!reservationRepository.existsById(id)) {
            throw new ReservationDoesNotExistException("Reservation not found with id " + id);
        }
        reservationRepository.deleteById(id);
    }

    public List<TennisCourt> getAvailableTennisCourts(LocalDateTime startTime, LocalDateTime endTime) throws TennisCourtDoesNotExistsException, InvalidDateException {
        List<Reservation> reservations = reservationRepository.findAll();

        List<TennisCourt> availableTennisCourts = tennisCourtRepository.findAll().stream()
                .filter(tennisCourt -> reservations.stream()
                        .noneMatch(reservation ->
                                reservation.getTennisCourtName().equals(tennisCourt.getName()) &&
                                        ((reservation.getStartTime().equals(startTime) && reservation.getEndTime().equals(endTime)) ||
                                                (reservation.getStartTime().isBefore(endTime) && endTime.isBefore(reservation.getEndTime())) ||
                                                (reservation.getStartTime().isBefore(startTime) && startTime.isBefore(reservation.getEndTime()))))) //filters out tennis courts that have overlapping reservations
                .collect(Collectors.toList()); //collects the filtered tennis courts into a list

        if (availableTennisCourts.isEmpty()) {
            throw new TennisCourtDoesNotExistsException("No tennis courts available");
        }
        return availableTennisCourts;
    }
}
