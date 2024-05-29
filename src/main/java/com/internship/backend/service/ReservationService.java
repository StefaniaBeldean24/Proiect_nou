package com.internship.backend.service;

import com.internship.backend.dto.ReservationDTO;
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

    public Reservation addReservation(Reservation reservation){
        return reservationRepository.save(reservation);
    }

    public Reservation fromDTO(ReservationDTO reservationDTO) {
        Reservation reservation = new Reservation();
        reservation.setId(reservationDTO.getId());
        reservation.setStartTime(reservationDTO.getStartTime());
        reservation.setEndTime(reservationDTO.getEndTime());

        for(Users user: userRepository.findAll()) {
            if(user.getId() == reservationDTO.getUserId()) reservation.setUser(user);
        }

        for(TennisCourt tennisCourt: tennisCourtRepository.findAll()){
            if(tennisCourt.getId() == reservationDTO.getTennisCourtId()) reservation.setTennisCourt(tennisCourt);
        }
        return reservation;

    }

    public Reservation update(int reservationId, Reservation newReservation){
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(()->new EntityNotFoundException("Reservation not found"));

        reservation.setStartTime(newReservation.getStartTime());
        reservation.setEndTime(newReservation.getEndTime());

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

}
