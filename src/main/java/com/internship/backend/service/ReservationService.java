package com.internship.backend.service;

import com.internship.backend.dto.ReservationDTO;
import com.internship.backend.exceptions.InvalidDateException;
import com.internship.backend.exceptions.ReservationAlreadyExists;
import com.internship.backend.exceptions.ReservationDoesNotExistException;
import com.internship.backend.exceptions.TennisCourtDoesNotExistsException;
import com.internship.backend.model.NewDate;
import com.internship.backend.model.Reservation;
import com.internship.backend.model.TennisCourt;
import com.internship.backend.model.Users;
import com.internship.backend.repository.PriceRepository;
import com.internship.backend.repository.ReservationRepository;
import com.internship.backend.repository.TennisCourtRepository;
import com.internship.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
            return reservationRepository.save(reservation);
        }
        else{
            throw new ReservationAlreadyExists("Reservation intersects another one");
        }
    }

    public boolean isValidReservation(Reservation reservation){
        List<Reservation> reservationList = reservationRepository.findAll();
        for(Reservation elem : reservationList){
            if(elem.getTennisCourt().getId() == reservation.getTennisCourt().getId()) {
                if (!(reservation.getEndTime().isBefore(elem.getStartTime()) || reservation.getStartTime().isAfter(elem.getEndTime()))) {
                    return false;
                }
            }
        }
        return true;
    }


    public Reservation fromDTO(ReservationDTO reservationDTO) {
        Reservation reservation = new Reservation();
        int userId = reservationDTO.getUserId();
        Optional<Users> user = userRepository.findById(userId);
        reservation.setUser(user.get());

        reservation.setStartTime(reservationDTO.getStartTime());
        reservation.setEndTime(reservationDTO.getEndTime());

        int tennisCourtId = reservationDTO.getTennisCourtId();
        Optional<TennisCourt> tennisCourt = tennisCourtRepository.findById(tennisCourtId);
        reservation.setTennisCourt(tennisCourt.get());

        return reservation;

    }

    public Reservation update(int reservationId, Reservation newReservation) throws ReservationAlreadyExists {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(()->new ReservationAlreadyExists("Reservation not found"));

        reservation.setStartTime(newReservation.getStartTime());
        reservation.setEndTime(newReservation.getEndTime());
        if(reservationRepository.count()==0){
            reservationRepository.resetAutoIncrementId();
        }

        return reservationRepository.save(reservation);
    }

    public void delete(int reservationId) throws ReservationDoesNotExistException {
        if(!reservationRepository.existsById(reservationId))
            throw new ReservationDoesNotExistException("Reservation not found");
        priceRepository.deleteById(reservationId);
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
                tennisCourtId = elem.getTennisCourt().getId();

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

