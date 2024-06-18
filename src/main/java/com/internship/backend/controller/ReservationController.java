package com.internship.backend.controller;

import com.internship.backend.dto.ReservationDTO;
import com.internship.backend.exceptions.ReservationAlreadyExists;
import com.internship.backend.exceptions.ReservationDoesNotExistException;
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

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/reservations")
public class ReservationController {

    Logger Log = LoggerFactory.getLogger(ReservationController.class);

    @Autowired
    private ReservationService reservationService;

    @PostMapping("/add")
    public Reservation addReservation(@RequestBody ReservationDTO reservationDTO) throws ReservationAlreadyExists {
        Reservation reservation = reservationService.fromDTO(reservationDTO);
        reservationService.addReservation(reservation);
        return new ResponseEntity<>(reservation, HttpStatus.CREATED).getBody();
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Reservation>> getAllReservation(){
        Optional<List<Reservation>> reservation = Optional.ofNullable(reservationService.getAllReservations());
        if(reservation.isPresent()){
            return ok(reservationService.getAllReservations());
        }
        else{
            return notFound().build();
        }
    }

    @PostMapping("/available")
    public ResponseEntity<List<TennisCourt>> getAvailableTennisCourts(@RequestBody DateRangeRequest dateRangeRequest) {
        List<TennisCourt> availableTennisCourts = reservationService.getAvailableTennisCourts(dateRangeRequest.getStartDate(), dateRangeRequest.getEndDate());
        return ResponseEntity.ok(availableTennisCourts);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Reservation> updateReservation(@PathVariable("id") int reservationId, @RequestBody Reservation reservation){
        try{
            //Reservation reservation = reservationService.fromDTO(reservationDTO);
            Optional<Reservation> updatedReservation = Optional.ofNullable(reservationService.update(reservationId, reservation));
            Log.info("Get all"+ reservation);
            return ok(updatedReservation.get());
        }
        catch(ReservationAlreadyExists e){
            Log.error("Error processing update "+e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Price> deletePrice(@PathVariable("id") int reservationId){
        try{
            Log.info("Deleting reservation: "+ reservationId);
            reservationService.delete(reservationId);
            return ResponseEntity.ok().build();
        }catch (ReservationDoesNotExistException e) {
            Log.error("Error processing delete " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }


}
