package com.internship.backend.service;

import com.internship.backend.exceptions.InvalidDateException;
import com.internship.backend.exceptions.ReservationAlreadyExists;
import com.internship.backend.exceptions.ReservationDoesNotExistException;
import com.internship.backend.exceptions.TennisCourtDoesNotExistsException;
import com.internship.backend.model.NewDate;
import com.internship.backend.model.Reservation;
import com.internship.backend.model.TennisCourt;
import com.internship.backend.repository.PriceRepository;
import com.internship.backend.repository.ReservationRepository;
import com.internship.backend.repository.TennisCourtRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private TennisCourtRepository tennisCourtRepository;

    @Mock
    private PriceRepository priceRepository;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addReservation_validReservation() throws InvalidDateException, ReservationAlreadyExists {
        //arrange
        Reservation reservation = new Reservation();
        NewDate startTime = new NewDate(0, 17, 18, 7, 2024);
        NewDate endTime = new NewDate(0, 19, 18, 7, 2024);
        reservation.setStartTime(startTime);
        reservation.setEndTime(endTime);

        when(reservationRepository.findAll()).thenReturn(new ArrayList<>());
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        //act
        Reservation result = reservationService.addReservation(reservation);

        //assert
        assertNotNull(result);
        assertEquals(reservation, result);
    }

    @Test
    void addReservation_invalidReservation() throws InvalidDateException, ReservationAlreadyExists {
        //arrange
        Reservation reservation = new Reservation();
        NewDate startTime = new NewDate(0, 17, 18, 7, 2024);
        NewDate endTime = new NewDate(0, 20, 18, 7, 2024);
        reservation.setStartTime(startTime);
        reservation.setEndTime(endTime);
        reservation.setTennisCourt(new TennisCourt());

        //act
        InvalidDateException exception = assertThrows(InvalidDateException.class, () -> {
            reservationService.addReservation(reservationService.addReservation(reservation));
        });

        //assert
        assertEquals("The reservation duration must be exactly 2 hours", exception.getMessage());
    }

    @Test
    void getAllReservations() {
        //arrange
        List<Reservation> reservations = new ArrayList<>();

        when(reservationRepository.findAll()).thenReturn(reservations);

        //act
        List<Reservation> result = reservationService.getAllReservations();

        //assert
        assertEquals(result, reservations);
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void update_Valid() throws ReservationAlreadyExists {
        Reservation existingReservation = new Reservation();
        existingReservation.setId(1);
        Reservation newReservation = new Reservation();
        newReservation.setStartTime(new NewDate(0, 12, 1, 7, 2024));
        newReservation.setEndTime(new NewDate(0, 14, 1, 7, 2024));

        when(reservationRepository.findById(1)).thenReturn(Optional.of(existingReservation));
        when(reservationRepository.save(existingReservation)).thenReturn(existingReservation);

        Reservation result = reservationService.update(1, newReservation);
        assertEquals(newReservation.getStartTime(), result.getStartTime());
        assertEquals(newReservation.getEndTime(), result.getEndTime());
    }

    @Test
    void update_ReservationDoesNotExist() {
        when(reservationRepository.findById(1)).thenReturn(Optional.empty());

        ReservationAlreadyExists exception = assertThrows(ReservationAlreadyExists.class, () -> reservationService.update(1, new Reservation()));
        assertEquals("Reservation not found", exception.getMessage());
    }

    @Test
    void delete_Valid() throws ReservationDoesNotExistException {
        when(reservationRepository.existsById(1)).thenReturn(true);
        when(reservationRepository.count()).thenReturn(0L);

        reservationService.delete(1);

        verify(reservationRepository, times(1)).deleteById(1);
        verify(priceRepository, times(1)).deleteById(1);
        verify(reservationRepository, times(1)).resetAutoIncrementId();
    }

    @Test
    void delete_ReservationDoesNotExist() {
        when(reservationRepository.existsById(1)).thenReturn(false);

        ReservationDoesNotExistException exception = assertThrows(ReservationDoesNotExistException.class, () -> reservationService.delete(1));
        assertEquals("Reservation not found", exception.getMessage());
    }

    @Test
    void getAvailableTennisCourts_Valid() throws TennisCourtDoesNotExistsException, InvalidDateException {
        NewDate startDate = new NewDate(0, 12, 1, 7, 2024);
        NewDate endDate = new NewDate(0, 14, 1, 7, 2024);
        List<TennisCourt> tennisCourts = List.of(new TennisCourt());
        List<Reservation> reservations = List.of();

        when(tennisCourtRepository.findAll()).thenReturn(tennisCourts);
        when(reservationRepository.findAll()).thenReturn(reservations);

        List<TennisCourt> result = reservationService.getAvailableTennisCourts(startDate, endDate);
        assertEquals(tennisCourts, result);
    }

    @Test
    void getAvailableTennisCourts_InvalidDate() {
        NewDate startDate = new NewDate(0, 12, 1, 7, 2024);
        NewDate endDate = new NewDate(0, 15, 1, 7, 2024);

        InvalidDateException exception = assertThrows(InvalidDateException.class, () -> reservationService.getAvailableTennisCourts(startDate, endDate));
        assertEquals("The reservation duration must be exactly 2 hours", exception.getMessage());
    }
}
