package com.internship.backend.controller;

import com.internship.backend.dto.ReservationDTO;
import com.internship.backend.exceptions.*;
import com.internship.backend.model.Reservation;
import com.internship.backend.model.TennisCourt;
import com.internship.backend.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.ResponseEntity.*;


@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping("/add")
    public ResponseEntity<Reservation> addReservation(@RequestBody ReservationDTO reservationDTO) {
        try {
            return ok(reservationService.addReservation(reservationDTO));
        } catch (ReservationAlreadyExists | InvalidDateException | UserDoesNotExistException |
                 TennisCourtDoesNotExistsException e) {
            return status(CONFLICT).body(null);
        }
    }

    @GetMapping("/getAll")
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @GetMapping("/{id}")
    public Optional<Reservation> getReservationById(@PathVariable String id) {
        return reservationService.getReservationById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Reservation> updateReservation(@RequestBody Reservation reservation) {
        try {
            return ok(reservationService.updateReservation(reservation));
        } catch (ReservationDoesNotExistException e) {
            return status(CONFLICT).body(null);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteReservation(@PathVariable String id) {
        try {
            reservationService.deleteReservation(id);
            return ok().build();
        } catch (ReservationDoesNotExistException e) {
            return status(CONFLICT).body(null);
        }
    }

    @PostMapping("/available")
    public ResponseEntity<List<TennisCourt>> getAvailableTennisCourts(@RequestParam("startTime") String startTimeString, @RequestParam("endTime") String endTimeString) {
        try {
            LocalDateTime startTime = LocalDateTime.parse(startTimeString);
            LocalDateTime endTime = LocalDateTime.parse(endTimeString);
            return ok(reservationService.getAvailableTennisCourts(startTime, endTime));
        } catch (InvalidDateException | TennisCourtDoesNotExistsException e) {
            return notFound().build();
        }
    }
}
