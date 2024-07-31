package com.internship.backend.service;

import com.internship.backend.dto.ReservationDTO;
import com.internship.backend.exceptions.*;
import com.internship.backend.mappper.ReservationMapper;
import com.internship.backend.model.Reservation;
import com.internship.backend.model.ReservationValidator;
import com.internship.backend.model.TennisCourt;
import com.internship.backend.model.User;
import com.internship.backend.repository.PriceRepository;
import com.internship.backend.repository.ReservationRepository;
import com.internship.backend.repository.TennisCourtRepository;
import com.internship.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    private ReservationMapper reservationMapper = new ReservationMapper();

    public List<Reservation> getAllReservations(){
        return reservationRepository.findAll();
    }

    public Reservation addReservation(ReservationDTO reservationDTO) throws ReservationAlreadyExists, InvalidDateException, TennisCourtDoesNotExistsException, UserDoesNotExistException {
        User user = userRepository.findById(reservationDTO.getUserId())
                .orElseThrow(()-> new UserDoesNotExistException("User not found"));

        TennisCourt tennisCourt = tennisCourtRepository.findById(reservationDTO.getTennisCourtId())
                .orElseThrow(()-> new TennisCourtDoesNotExistsException("TennisCourt not found"));

        Reservation reservation = reservationMapper.mapToReservation(reservationDTO, user, tennisCourt);

        ReservationValidator validator = new ReservationValidator(reservation);
        validator.validate();

        if (isValidReservation((reservation))) {
            return reservationRepository.save(reservation);
        } else {
            throw new ReservationAlreadyExists("Reservation intersects another one");
        }
    }

    private boolean isValidReservation(Reservation reservation){
        if (reservation == null) {
            return false;
        }

        LocalDateTime startTime = reservation.getStartTime();
        LocalDateTime endTime = reservation.getEndTime();

        if (startTime == null || endTime == null) {
            return false;
        }

        return startTime.isBefore(endTime);
    }

    public void resetAutoIncrement() {
        if (reservationRepository.count() == 0) {
            reservationRepository.resetAutoIncrementId();
        }
    }

    public void setTimeTennicCourtUser(Reservation reservation, LocalDateTime startTime, LocalDateTime endTime, TennisCourt tennisCourt, User user) {
        reservation.setStartTime(startTime);
        reservation.setEndTime(endTime);
        reservation.setTennisCourt(tennisCourt);
        reservation.setUser(user);
    }

    public Reservation update(int reservationId, ReservationDTO newReservationDTO) throws ReservationAlreadyExists, TennisCourtDoesNotExistsException, UserDoesNotExistException {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationAlreadyExists("Reservation not found"));

        User user = userRepository.findById(newReservationDTO.getUserId())
                .orElseThrow(() -> new UserDoesNotExistException("User not found"));

        TennisCourt tennisCourt = tennisCourtRepository.findById(newReservationDTO.getTennisCourtId())
                .orElseThrow(() -> new TennisCourtDoesNotExistsException("TennisCourt not found"));

        Reservation updatedReservation = reservationMapper.mapToReservation(newReservationDTO, user, tennisCourt);
        setTimeTennicCourtUser(updatedReservation, updatedReservation.getStartTime(), updatedReservation.getEndTime(), tennisCourt, user);

        resetAutoIncrement();

        return reservationRepository.save(reservation);
    }

    public void delete(int reservationId) throws ReservationDoesNotExistException {
        if (!reservationRepository.existsById(reservationId)) {
            throw new ReservationDoesNotExistException("Reservation not found");
        }

        reservationRepository.deleteById(reservationId);
        priceRepository.deleteById(reservationId);

       resetAutoIncrement();
    }

    public void getAvailableTennisCourtsValidation(LocalDateTime startDate, LocalDateTime endDate) throws InvalidDateException {
        if (!startDate.toLocalDate().equals(endDate.toLocalDate())){
            throw new InvalidDateException("The start date and end date must be within the same day");
        }

        if(endDate.getHour() - startDate.getHour() != 2){
            throw new InvalidDateException("The reservation duration must be exactly 2 hours");
        }
    }

   public List<TennisCourt> getAvailableTennisCourts(LocalDateTime startDate, LocalDateTime endDate) throws TennisCourtDoesNotExistsException, InvalidDateException {
        int tennisCourtId;
        List<TennisCourt> tennisCourts = tennisCourtRepository.findAll();
        List<Reservation> reservations = reservationRepository.findAll();

        getAvailableTennisCourtsValidation(startDate, endDate);

        for (Reservation elem : reservations){
            if (( elem.getStartTime().equals(startDate) &&  elem.getEndTime().equals(endDate))
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

        if (!tennisCourts.isEmpty()) {
            return tennisCourts;
        } else {
            throw new TennisCourtDoesNotExistsException("No tennis courts available");
        }
    }
}

