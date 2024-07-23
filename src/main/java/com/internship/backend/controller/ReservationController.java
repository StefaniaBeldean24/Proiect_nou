package com.internship.backend.controller;

import com.internship.backend.dto.ReservationDTO;
import com.internship.backend.exceptions.InvalidDateException;
import com.internship.backend.exceptions.ReservationAlreadyExists;
import com.internship.backend.exceptions.ReservationDoesNotExistException;
import com.internship.backend.exceptions.TennisCourtDoesNotExistsException;
import com.internship.backend.model.*;
import com.internship.backend.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

import static org.springframework.http.ResponseEntity.*;


@RestController
@RequestMapping("api/reservations")
public class ReservationController {

    Logger Log = LoggerFactory.getLogger(ReservationController.class);

    @Autowired
    private ReservationService reservationService;

    @PostMapping("/add")
    public ResponseEntity<Reservation> addReservation(@RequestBody ReservationDTO reservationDTO) {
        try{
            return ok(reservationService.addReservation(reservationService.parse(reservationDTO)));
        }catch(ReservationAlreadyExists e){
            Log.error("Reservation already exists " + e.getMessage());
            return status(HttpStatus.BAD_REQUEST).body(null);
        }catch(InvalidDateException e){
            Log.error("Invalid date "+ e.getMessage());
            return status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Reservation>> getAllReservation(){
        if(ofNullable(reservationService.getAllReservations()).isPresent()){
            return ok(reservationService.getAllReservations());
        }
        else{
            return notFound().build();
        }
    }

    @PostMapping("/available")
    public ResponseEntity<List<TennisCourt>> getAvailableTennisCourts(@RequestBody DateRangeRequest dateRangeRequest) {
        try{
            return ResponseEntity.ok(reservationService.getAvailableTennisCourts(dateRangeRequest.start(), dateRangeRequest.end()));
        }catch(TennisCourtDoesNotExistsException e){
            Log.error("No available tennis courts "+e.getMessage());
            return notFound().build();
        }catch (InvalidDateException e){
            Log.error("Invalid date " + e.getMessage());
            return notFound().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Optional<Reservation>> updateReservation(@PathVariable("id") int reservationId, @RequestBody Reservation reservation){
        try{
            var updatedReservation = ofNullable(reservationService.update(reservationId, reservation));
            Log.info("Updating "+ reservation);
            return ok(updatedReservation);
        }
        catch(ReservationAlreadyExists e){
            Log.error("Error processing update "+e.getMessage());
            return notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Price> deletePrice(@PathVariable("id") int reservationId){
        try{
            Log.info("Deleting reservation: "+ reservationId);
            reservationService.delete(reservationId);
            return ok().build();
        }catch (ReservationDoesNotExistException e) {
            Log.error("Error processing delete " + e.getMessage());
            return notFound().build();
        }
    }


}
