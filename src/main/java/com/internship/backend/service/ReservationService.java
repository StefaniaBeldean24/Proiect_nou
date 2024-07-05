package com.internship.backend.service;

import com.internship.backend.controller.UserController;
import com.internship.backend.exceptions.*;
import com.internship.backend.model.NewDate;
import com.internship.backend.model.Reservation;

import com.internship.backend.model.TennisCourt;
import com.internship.backend.model.Users;
import com.internship.backend.repository.ReservationRepository;

import com.internship.backend.repository.TennisCourtRepository;
import com.internship.backend.repository.UserRepository;
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
    private UserRepository userRepository;

    @Autowired
    private TennisCourtRepository tennisCourtRepository;


//    public Reservation createReservation(Reservation reservation) throws UserDoesNotExistException {
//        reservation.setId(idGeneratorService.getCurrentId());
//
//        Optional<Users> optionalUser = userRepository.findById(reservation.getUserId());
//        if(optionalUser.isPresent()) {
//            Users user = optionalUser.get();
//            List<Reservation> reservations = user.getReservations();
//            if(Objects.isNull(reservations)) {
//                reservations = new ArrayList<>();
//            }
//            reservations.add(reservation);
//            user.setReservations(reservations);
//            userRepository.save(user);
//        }else{
//            throw new UserDoesNotExistException("User not found with id" + reservation.getUserId());
//        }
//
//        return reservationRepository.save(reservation);
//    }

    public Reservation addReservation(Reservation reservation) throws ReservationAlreadyExists, InvalidDateException {

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

        if (isValidReservation((reservation)))
        {
            reservation.setId(idGeneratorService.getCurrentId());
            return reservationRepository.save(reservation);
        }
        else{
            throw new ReservationAlreadyExists("Reservation intersects another one");
        }
    }

    public boolean isValidReservation(Reservation reservation){
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
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);
        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();
            reservation.setUserId(reservationDetails.getUserId());
            reservation.setTennisCourtId(reservationDetails.getTennisCourtId());
            reservation.setStartTime(reservationDetails.getStartTime());
            reservation.setEndTime(reservationDetails.getEndTime());
            return reservationRepository.save(reservation);
        } else {
            throw new ReservationDoesNotExistException("Reservation not found with id " + id);
        }
    }

    public void deleteReservation(Integer id) throws ReservationDoesNotExistException {
        reservationRepository.deleteById(id);
    }

    //metoda in care user-ul sa vada toate terenurile disponibile dintr-o anumita data
    public List<TennisCourt> getAvailableTennisCourts(NewDate startDate, NewDate endDate) throws TennisCourtDoesNotExistsException, InvalidDateException {
        int tennisCourtId;
        List<TennisCourt> tennisCourts = tennisCourtRepository.findAll();
        List<Reservation> reservations = reservationRepository.findAll();

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
