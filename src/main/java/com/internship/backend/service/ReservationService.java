package com.internship.backend.service;

import com.internship.backend.dto.ReservationDTO;
import com.internship.backend.exceptions.ReservationAlreadyExists;
import com.internship.backend.model.Reservation;
import com.internship.backend.model.TennisCourt;
import com.internship.backend.model.Users;
import com.internship.backend.repository.PriceRepository;
import com.internship.backend.repository.ReservationRepository;
import com.internship.backend.repository.TennisCourtRepository;
import com.internship.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TennisCourtRepository tennisCourtRepository;
    @Autowired
    private PriceRepository priceRepository;

    public List<Reservation> getAllReservations(){
        return reservationRepository.findAll();
    }

    public Reservation addReservation(Reservation reservation) throws ReservationAlreadyExists {

        /*for (Reservation elem : reservationRepository.findAll()){
            if(elem.getDay() == reservation.getDay() && elem.getMonth() == reservation.getMonth() && elem.getYear() == reservation.getYear()
            && elem.getStartHour() == reservation.getStartHour() && elem.getEndHour() == reservation.getEndHour()){
                throw new ReservationAlreadyExists("Reservation already exists");
            }
        }*/
        return reservationRepository.save(reservation);
    }

    public Reservation fromDTO(ReservationDTO reservationDTO) {
        Reservation reservation = new Reservation();
        reservation.setId(reservationDTO.getId());
        reservation.setStartTime(reservationDTO.getStartTime());
        reservation.setEndTime(reservationDTO.getEndTime());

//        reservation.setYear(reservationDTO.getYear());
//        reservation.setMonth(reservationDTO.getMonth());
//        reservation.setDay(reservationDTO.getDay());
//        reservation.setStartHour(reservationDTO.getStartHour());
//        reservation.setEndHour(reservationDTO.getEndHour());

        for(Users user: userRepository.findAll()) {
            if(user.getId() == reservationDTO.getId()) reservation.setUser(user);
        }

        for(TennisCourt tennisCourt: tennisCourtRepository.findAll()){
            if(tennisCourt.getId() == reservationDTO.getId()) reservation.setTennisCourt(tennisCourt);
        }
        return reservation;

    }

    public Reservation update(int reservationId, Reservation newReservation){
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(()->new EntityNotFoundException("Reservation not found"));

        reservation.setStartTime(newReservation.getStartTime());
        reservation.setEndTime(newReservation.getEndTime());

//        reservation.setYear(reservation.getYear());
//        reservation.setMonth(reservation.getMonth());
//        reservation.setDay(reservation.getDay());
//        reservation.setStartHour(reservation.getStartHour());
//        reservation.setEndHour(reservation.getEndHour());

        if(reservationRepository.count()==0){
            reservationRepository.resetAutoIncrementId();
        }

        return reservationRepository.save(reservation);
    }

    public void delete(int reservationId){
        if(!reservationRepository.existsById(reservationId))
            throw new RuntimeException("Reservation not found");
        priceRepository.deleteById(reservationId);
    }

   /* public List<Integer> getReservationsByDate(int month, int day, int startHour, int endHour){

        List<Reservation> reservations = reservationRepository.findAll();
        List<Integer> tennisCourts = new ArrayList<>();
        for(Reservation reservation: reservations){

            if(reservation.getMonth() == reservations.get(month).getMonth() &&
                    reservation.getDay() == reservations.get(day).getDay() &&
                    reservation.getStartHour() == reservations.get(startHour).getStartHour()
                    && reservation.getEndHour() == reservations.get(endHour).getEndHour()){

                tennisCourts.add(reservation.getId());
            }
        }
        return tennisCourts;
    }*/

}

