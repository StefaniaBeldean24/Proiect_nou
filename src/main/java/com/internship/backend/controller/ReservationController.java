package com.internship.backend.controller;

import com.internship.backend.exceptions.*;
import com.internship.backend.model.*;
import com.internship.backend.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;


@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping("/add")
    public ResponseEntity<Reservation> addReservation(@RequestBody Reservation reservation) {
        try{
            return ok(reservationService.addReservation(reservation));
        }catch(ReservationAlreadyExists | InvalidDateException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @GetMapping("/getAll")
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @GetMapping("/{id}")
    public Optional<Reservation> getReservationById(@PathVariable Integer id) {
        return reservationService.getReservationById(id);
    }

    @PutMapping("/update/{id}")
    public  ResponseEntity<Reservation> updateReservation(@PathVariable Integer id, @RequestBody Reservation reservation) {
        try{
            return ok(reservationService.updateReservation(id, reservation));
        }catch(ReservationDoesNotExistException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteReservation(@PathVariable Integer id) {
        try{
            reservationService.deleteReservation(id);
            return ResponseEntity.ok().build();
        }catch(ReservationDoesNotExistException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

    }

    @PostMapping("/available")
    public ResponseEntity<List<TennisCourt>> getAvailableTennisCourts(@RequestBody DateRangeRequest dateRangeRequest){
        try{
            List<TennisCourt> availableTennisCourts = reservationService.getAvailableTennisCourts(dateRangeRequest.getStartDate(), dateRangeRequest.getEndDate());
            return ResponseEntity.ok(availableTennisCourts);
        }catch(InvalidDateException | TennisCourtDoesNotExistsException e){
            return ResponseEntity.notFound().build();
        }
    }
}
