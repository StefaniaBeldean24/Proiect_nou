package com.internship.backend.service;

import com.internship.backend.exceptions.*;
import com.internship.backend.model.NewDate;
import com.internship.backend.model.Reservation;

import com.internship.backend.model.TennisCourt;
import com.internship.backend.repository.ReservationRepository;

import com.internship.backend.repository.TennisCourtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private IdGeneratorService idGeneratorService;

    @Autowired
    private TennisCourtRepository tennisCourtRepository;


    public Reservation addReservation(Reservation reservation) throws ReservationAlreadyExists, InvalidDateException {

        reservationValidation(reservation);
        if (isValidReservation((reservation)))
        {
            reservation.setId(idGeneratorService.getCurrentId());
            return reservationRepository.save(reservation);
        }
        else{
            throw new ReservationAlreadyExists("Reservation intersects another one");
        }
    }

    private void reservationValidation(Reservation reservation) throws InvalidDateException {
        if (reservation.getStartTime().getMonth() != reservation.getEndTime().getMonth()) {
            throw new InvalidDateException("The start date and end date must be within the same month");
        }

        if(reservation.getStartTime().getDay() != reservation.getEndTime().getDay()){
            throw new InvalidDateException("The start date and end date must be within the same day");
        }

        if(reservation.getStartTime().getYear() != reservation.getEndTime().getYear()){
            throw new InvalidDateException("The start date and end date must be within the same year");
        }

        if(reservation.getEndTime().getHour() - reservation.getStartTime().getHour() != 2){
            throw new InvalidDateException("The reservation duration must be exactly 2 hours");
        }
    }

    private void dateValidation(NewDate startDate, NewDate endDate) throws InvalidDateException {
        if (startDate.getMonth() != endDate.getMonth()) {
            throw new InvalidDateException("The start date and end date must be within the same month");
        }

        if(startDate.getDay() != endDate.getDay()){
            throw new InvalidDateException("The start date and end date must be within the same day");
        }

        if(startDate.getYear() != endDate.getYear()){
            throw new InvalidDateException("The start date and end date must be within the same year");
        }

        if(endDate.getHour() - startDate.getHour() != 2){
            throw new InvalidDateException("The reservation duration must be exactly 2 hours");
        }
    }

    private boolean isValidReservation(Reservation reservation){
        List<Reservation> reservationList = reservationRepository.findAll();
        for(Reservation elem : reservationList){
            if(elem.getTennisCourtId() == reservation.getTennisCourtId()) {
                if (!(reservation.getEndTime().isBefore(elem.getStartTime()) || reservation.getStartTime().isAfter(elem.getEndTime()))) {
                    return false;
                }
            }
        }
        return true;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }


    public Optional<Reservation> getReservationById(Integer id) {
        return reservationRepository.findById(id);
    }

public Reservation updateReservation(Integer id, Reservation reservationDetails) throws ReservationDoesNotExistException {
    return reservationRepository.findById(id)
            .map(reservation -> tryUpdateReservation(reservation, reservationDetails))
            .orElseThrow(() -> new ReservationDoesNotExistException("Reservation not found with id " + id));
}

    private Reservation tryUpdateReservation(Reservation reservation, Reservation reservationDetails) {
        reservation.setUserId(reservationDetails.getUserId());
        reservation.setTennisCourtId(reservationDetails.getTennisCourtId());
        reservation.setStartTime(reservationDetails.getStartTime());
        reservation.setEndTime(reservationDetails.getEndTime());
        return reservationRepository.save(reservation);
    }

    public void deleteReservation(Integer id) throws ReservationDoesNotExistException {
        if(!reservationRepository.existsById(1)){
            throw new ReservationDoesNotExistException("Reservation not found with id " + id);
        }
        reservationRepository.deleteById(id);
    }

    //metoda in care user-ul sa vada toate terenurile disponibile dintr-o anumita data
    public List<TennisCourt> getAvailableTennisCourts(NewDate startDate, NewDate endDate) throws TennisCourtDoesNotExistsException, InvalidDateException {
        int tennisCourtId;
        List<TennisCourt> tennisCourts = tennisCourtRepository.findAll();
        List<Reservation> reservations = reservationRepository.findAll();

        dateValidation(startDate, endDate);
        for (Reservation elem : reservations){
            if(( elem.getStartTime().equals(startDate) &&  elem.getEndTime().equals(endDate))
                    || (elem.getStartTime().isBefore(endDate) && endDate.isBefore(elem.getEndTime()))
                    || (elem.getStartTime().isBefore(startDate) && startDate.isBefore(elem.getEndTime())))
            {
                tennisCourtId = elem.getTennisCourtId();

                for (int i = 0; i < tennisCourts.size(); i++) {
                    if (tennisCourts.get(i).getId() == tennisCourtId) {
                        tennisCourts.remove(i);
                    }
                }
            }
        }
        if(!tennisCourts.isEmpty()){
            return tennisCourts;
        }
        else{
            throw new TennisCourtDoesNotExistsException("No tennis courts available");
        }
    }

}
