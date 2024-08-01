package com.internship.backend.controller;

import com.internship.backend.dto.ReservationDTO;
import com.internship.backend.exceptions.*;
import com.internship.backend.model.DateRangeRequest;
import com.internship.backend.model.Price;
import com.internship.backend.model.Reservation;
import com.internship.backend.model.TennisCourt;
import com.internship.backend.service.ReservationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.ResponseEntity.*;


@RestController
@RequestMapping("api/reservations")
public class ReservationController {

    Logger Log = LoggerFactory.getLogger(ReservationController.class);

    @Autowired
    private ReservationService reservationService;

    @PostMapping("/add")
    public ResponseEntity<Reservation> addReservation(@Valid @RequestBody ReservationDTO reservationDTO) {
        try {
            return ok(reservationService.addReservation(reservationDTO));
        } catch (ReservationAlreadyExists | UserDoesNotExistException | TennisCourtDoesNotExistsException e) {
            Log.error("Reservation already exists " + e.getMessage());
            return status(BAD_REQUEST).body(null);
        } catch (InvalidDateException e) {
            Log.error("Invalid date " + e.getMessage());
            return status(BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Reservation>> getAllReservation() {
        List<Reservation> reservations = reservationService.getAllReservations();
        if (ofNullable(reservations).isPresent()) {
            Log.info("Get all reservations: ");
            return ok(reservations);
        } else {
            return notFound().build();
        }
    }

    @PostMapping("/available")
    public ResponseEntity<List<TennisCourt>> getAvailableTennisCourts(@RequestBody DateRangeRequest dateRangeRequest) {
        try {
            return ResponseEntity.ok(reservationService.getAvailableTennisCourts(dateRangeRequest.start(), dateRangeRequest.end()));
        } catch (TennisCourtDoesNotExistsException e) {
            Log.error("No available tennis courts " + e.getMessage());
            return notFound().build();
        } catch (InvalidDateException e) {
            Log.error("Invalid date " + e.getMessage());
            return notFound().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Optional<Reservation>> updateReservation(@PathVariable("id") int reservationId, @RequestBody ReservationDTO reservationDTO) {
        try {
            var updatedReservation = ofNullable(reservationService.update(reservationId, reservationDTO));
            Log.info("Updating " + reservationDTO);
            return ok(updatedReservation);
        } catch (ReservationAlreadyExists | TennisCourtDoesNotExistsException | UserDoesNotExistException e) {
            Log.error("Error processing update " + e.getMessage());
            return notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Price> deletePrice(@PathVariable("id") int reservationId) {
        try {
            Log.info("Deleting reservation: " + reservationId);
            reservationService.delete(reservationId);
            return ok().build();
        } catch (ReservationDoesNotExistException e) {
            Log.error("Error processing delete " + e.getMessage());
            return notFound().build();
        }
    }
}
