package com.internship.backend.service;

import com.internship.backend.dto.ReservationDTO;
import com.internship.backend.exceptions.*;
import com.internship.backend.model.Reservation;
import com.internship.backend.model.ReservationValidator;
import com.internship.backend.model.TennisCourt;
import com.internship.backend.model.User;
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

    public Reservation addReservation(final ReservationDTO reservationDTO) throws ReservationAlreadyExists, InvalidDateException, UserDoesNotExistException, TennisCourtDoesNotExistsException {
        validateReservationDTO(reservationDTO);

        if (isValidReservation(reservationDTO)) {
            return addReservation(buildReservation(reservationDTO));
        } else {
            throw new ReservationAlreadyExists("Reservation intersects another one");
        }
    }

    private void validateReservationDTO(final ReservationDTO reservationDTO) throws InvalidDateException {
        var validator = new ReservationValidator(reservationDTO);
        validator.validate();
    }

    private Reservation buildReservation(final ReservationDTO reservationDTO) throws UserDoesNotExistException, TennisCourtDoesNotExistsException {
        var user = findUser(reservationDTO);
        var tennisCourt = findTennisCourt(reservationDTO);

        return createReservation(reservationDTO, user, tennisCourt);
    }

    private User findUser(final ReservationDTO reservationDTO) throws UserDoesNotExistException {
        return userRepository.findByUsername(reservationDTO.getUserUsername())
                .orElseThrow(() -> new UserDoesNotExistException("User not found"));
    }

    private TennisCourt findTennisCourt(final ReservationDTO reservationDTO) throws TennisCourtDoesNotExistsException {
        return tennisCourtRepository.findByName(reservationDTO.getTennisCourtName())
                .orElseThrow(() -> new TennisCourtDoesNotExistsException("Tennis Court not found"));
    }

    private Reservation createReservation(final ReservationDTO reservationDTO, final User user, final TennisCourt tennisCourt) {
        var reservation = new Reservation();
        reservation.setId(UUID.randomUUID().toString());
        reservation.setTennisCourtName(tennisCourt.getName());
        reservation.setUserUsername(user.getUsername());
        reservation.setStartTime(reservationDTO.getStartTime());
        reservation.setEndTime(reservationDTO.getEndTime());
        return reservation;
    }

    public Reservation addReservation(final Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    private boolean isValidReservation(final ReservationDTO reservationDTO) {
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

        return !hasOverlappingReservation(reservationDTO, startTime, endTime);
    }

    private boolean hasOverlappingReservation(final ReservationDTO reservationDTO, final LocalDateTime startTime, final LocalDateTime endTime) {
        return reservationRepository.findAll().stream()
                .anyMatch(reservation ->
                        reservation.getTennisCourtName().equals(reservationDTO.getTennisCourtName()) &&
                                ((reservation.getStartTime().isBefore(endTime) && reservation.getEndTime().isAfter(startTime)) ||
                                        (reservation.getStartTime().isBefore(startTime) && reservation.getEndTime().isAfter(endTime)) ||
                                        (reservation.getStartTime().equals(startTime) && reservation.getEndTime().equals(endTime))));
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Optional<Reservation> getReservationById(final String id) {
        return reservationRepository.findById(id);
    }

    public Reservation updateReservation(final Reservation reservationDetails) throws ReservationDoesNotExistException {
        return reservationRepository.findById(reservationDetails.getId())
                .map(reservation -> updateOldReservation(reservation, reservationDetails))
                .orElseThrow(() -> new ReservationDoesNotExistException("Reservation not found"));
    }

    private Reservation updateOldReservation(final Reservation reservation, final Reservation reservationDetails) {
        reservation.setUserUsername(reservationDetails.getUserUsername());
        reservation.setTennisCourtName(reservationDetails.getTennisCourtName());
        reservation.setStartTime(reservationDetails.getStartTime());
        reservation.setEndTime(reservationDetails.getEndTime());
        return reservationRepository.save(reservation);
    }

    public void deleteReservation(final String id) throws ReservationDoesNotExistException {
        if (!reservationRepository.existsById(id)) {
            throw new ReservationDoesNotExistException("Reservation not found with id " + id);
        }
        reservationRepository.deleteById(id);
    }

    public List<TennisCourt> getAvailableTennisCourts(final LocalDateTime startTime, final LocalDateTime endTime) throws TennisCourtDoesNotExistsException, InvalidDateException {
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
