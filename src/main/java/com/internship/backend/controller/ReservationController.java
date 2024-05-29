package com.internship.backend.controller;

import com.internship.backend.dto.PriceDTO;
import com.internship.backend.dto.ReservationDTO;
import com.internship.backend.model.Price;
import com.internship.backend.model.Reservation;
import com.internship.backend.service.ReservationService;
import jakarta.persistence.EntityNotFoundException;
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
    public Reservation addReservation(@RequestBody ReservationDTO reservatonDTO){
        Reservation reservation = reservationService.fromDTO(reservatonDTO);
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

    @PutMapping("/update/{id}")
    public ResponseEntity<Reservation> updateReservation(@PathVariable("id") int reservationId, @RequestBody ReservationDTO reservationDTO){
        try{
            Reservation reservation = reservationService.fromDTO(reservationDTO);
            Optional<Reservation> updatedReservation = Optional.ofNullable(reservationService.update(reservationId, reservation));
            Log.info("Get all"+ reservation.toString());
            return ok(updatedReservation.get());
        }
        catch(EntityNotFoundException e){
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
        }catch (RuntimeException e) {
            Log.error("Error processing delete " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
