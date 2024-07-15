package com.internship.backend.service;

import com.internship.backend.exceptions.InvalidDateException;
import com.internship.backend.exceptions.ReservationAlreadyExists;
import com.internship.backend.exceptions.ReservationDoesNotExistException;
import com.internship.backend.exceptions.TennisCourtDoesNotExistsException;
import com.internship.backend.model.NewDate;
import com.internship.backend.model.Reservation;
import com.internship.backend.model.TennisCourt;
import com.internship.backend.repository.ReservationRepository;
import com.internship.backend.repository.TennisCourtRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private IdGeneratorService idGeneratorService;

    @Mock
    private TennisCourtRepository tennisCourtRepository;

    @InjectMocks
    private ReservationService reservationService;

    private Reservation reservation;
    private NewDate startDate;
    private NewDate endDate;

    @BeforeEach
    void setUp() {
        startDate = new NewDate(0,10,1,7,2024);
        endDate = new NewDate(0,12,1,7,2024);
        reservation = new Reservation();
        reservation.setId(1);
        reservation.setUserId(2);
        reservation.setTennisCourtId(3);
        reservation.setStartTime(startDate);
        reservation.setEndTime(endDate);
    }

    @Test
    void addReservation_validReservation() throws InvalidDateException, ReservationAlreadyExists {
        //arrange
        when(reservationRepository.findAll()).thenReturn(new ArrayList<>());
        when(idGeneratorService.getCurrentId()).thenReturn(1);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        //act
        Reservation savedReservation = reservationService.addReservation(reservation);

        //assert
        assertNotNull(savedReservation);
        assertEquals(1, savedReservation.getId());
        verify(reservationRepository, times(1)).save(reservation);


    }

    @Test
    void addReservation_invalidReservation() throws InvalidDateException, ReservationAlreadyExists {
        //arrange
        reservation.getEndTime().setHour(17);


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
        reservations.add(reservation);

        when(reservationRepository.findAll()).thenReturn(reservations);

        //act
        List<Reservation> result = reservationService.getAllReservations();

        //assert
        assertNotNull(result);
        assertEquals(result.size(), 1);
        assertTrue(result.contains(reservation));
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void isValidReservation_nonOverlapping() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //arrange
        Reservation reservation1 = new Reservation();
        reservation1.setId(4);
        reservation1.setUserId(2);
        reservation1.setTennisCourtId(9);
        reservation1.setStartTime(new NewDate(0, 14, 1, 7, 2024));
        reservation1.setEndTime(new NewDate(0, 16, 1, 7, 2024));

        when(reservationRepository.findAll()).thenReturn(List.of(reservation1));
        //use reflection to test a provate method
        Method method = ReservationService.class.getDeclaredMethod("isValidReservation", Reservation.class);
        method.setAccessible(true);

        boolean result = (boolean) method.invoke(reservationService, reservation);
        //we make sure that the new reservation does not overlap with an existing one
        assertTrue(result);
    }

    @Test
    void isValidReservation_overlapping() throws Exception {
        //arrange
        Reservation overlappingReservation = new Reservation();
        overlappingReservation.setId(4);
        overlappingReservation.setUserId(2);
        overlappingReservation.setTennisCourtId(3);
        overlappingReservation.setStartTime(new NewDate(0, 9, 1, 7, 2024));
        overlappingReservation.setEndTime(new NewDate(0, 11, 1, 7, 2024));

        when(reservationRepository.findAll()).thenReturn(Arrays.asList(overlappingReservation));

        //act
        java.lang.reflect.Method method = ReservationService.class.getDeclaredMethod("isValidReservation", Reservation.class);
        method.setAccessible(true);

        boolean result = (boolean) method.invoke(reservationService, reservation);
        //assert
        assertFalse(result);
    }

    @Test
    void isValidReservation_overlappingStartTime() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //arrange
        Reservation overlappingStartTimeReservation = new Reservation();
        overlappingStartTimeReservation.setId(4);
        overlappingStartTimeReservation.setUserId(2);
        overlappingStartTimeReservation.setTennisCourtId(3);
        overlappingStartTimeReservation.setStartTime(new NewDate(0, 11, 1, 7, 2024));
        overlappingStartTimeReservation.setEndTime(new NewDate(0, 13, 1, 7, 2024));

        when(reservationRepository.findAll()).thenReturn(List.of(overlappingStartTimeReservation));

        //act
        Method method = Reservation.class.getDeclaredMethod("isValidReservation", Reservation.class);
        method.setAccessible(true);

        //assert
        boolean result = (boolean) method.invoke(reservationService, reservation);
        assertFalse(result);
    }

    @Test
    void isValidReservation_overlappingEndTime() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //arrange
        Reservation overlappingEndTimeReservation = new Reservation();
        overlappingEndTimeReservation.setId(4);
        overlappingEndTimeReservation.setUserId(2);
        overlappingEndTimeReservation.setTennisCourtId(3);
        overlappingEndTimeReservation.setStartTime(new NewDate(0, 9, 1, 7, 2024));
        overlappingEndTimeReservation.setEndTime(new NewDate(0, 11, 1, 7, 2024));

        when(reservationRepository.findAll()).thenReturn(List.of(overlappingEndTimeReservation));

        Method method = ReservationService.class.getDeclaredMethod("isValidReservation", Reservation.class);
        //act
        method.setAccessible(true);

        boolean result = (boolean) method.invoke(reservationService, reservation);
        //assert
        assertFalse(result);
    }


    @Test
    void isValidReservation_containedWithinAnother() throws Exception {
        //arrgange
        Reservation containedWithinAnotherReservation = new Reservation();
        containedWithinAnotherReservation.setId(2);
        containedWithinAnotherReservation.setUserId(2);
        containedWithinAnotherReservation.setTennisCourtId(3);
        containedWithinAnotherReservation.setStartTime(new NewDate(0, 9, 1, 7, 2024));
        containedWithinAnotherReservation.setEndTime(new NewDate(0, 15, 1, 7, 2024));

        when(reservationRepository.findAll()).thenReturn(List.of(containedWithinAnotherReservation));

        //act
        java.lang.reflect.Method method = ReservationService.class.getDeclaredMethod("isValidReservation", Reservation.class);
        method.setAccessible(true);

        boolean result = (boolean) method.invoke(reservationService, reservation);
        //assert
        assertFalse(result);
    }


    @Test
    void isValidReservation_containingAnother() throws Exception {
        //arrange
        Reservation containingAnotherReservation = new Reservation();
        containingAnotherReservation.setId(2);
        containingAnotherReservation.setUserId(2);
        containingAnotherReservation.setTennisCourtId(3);
        containingAnotherReservation.setStartTime(new NewDate(0, 11, 1, 7, 2024));
        containingAnotherReservation.setEndTime(new NewDate(0, 12, 1, 7, 2024));

        when(reservationRepository.findAll()).thenReturn(List.of(containingAnotherReservation));

        //act
        java.lang.reflect.Method method = ReservationService.class.getDeclaredMethod("isValidReservation", Reservation.class);
        method.setAccessible(true);

        boolean result = (boolean) method.invoke(reservationService, reservation);
        //assert
        assertFalse(result);
    }


    @Test
    void getAvailableTennisCourts_validDates() throws InvalidDateException, TennisCourtDoesNotExistsException {
        //arrange
        TennisCourt court1 = new TennisCourt(5, "tennisCourt1", "details", null, null);
        TennisCourt court2 = new TennisCourt(6, "tennisCourt2", "details", null, null);
        List<TennisCourt> courts = new ArrayList<>();
        courts.add(court1);
        courts.add(court2);

        when(tennisCourtRepository.findAll()).thenReturn(courts);
        when(reservationRepository.findAll()).thenReturn(new ArrayList<>());


        //act
        List<TennisCourt> availableCourts = reservationService.getAvailableTennisCourts(startDate, endDate);

        //assert
        assertNotNull(availableCourts);
        assertEquals(2, availableCourts.size());
        verify(tennisCourtRepository, times(1)).findAll();
    }

    @Test
    void getAvailableTennisCourts_noAvailableCourts() {
        //arrange
        TennisCourt court1 = new TennisCourt(1, "Court 1", "details", null, null);
        List<TennisCourt> courts = new ArrayList<>();

        when(tennisCourtRepository.findAll()).thenReturn(courts);
        when(reservationRepository.findAll()).thenReturn(new ArrayList<>());

        //act
        TennisCourtDoesNotExistsException exception = assertThrows(TennisCourtDoesNotExistsException.class, () -> {
            reservationService.getAvailableTennisCourts(startDate, endDate);
        });

        //assert
        assertEquals("No tennis courts available", exception.getMessage());
        verify(tennisCourtRepository, times(1)).findAll();
    }

    @Test
    void updateReservation_validId() throws ReservationDoesNotExistException {
        //arrange
        Reservation newDetails = new Reservation();
        newDetails.setUserId(2);
        newDetails.setTennisCourtId(3);
        newDetails.setStartTime(startDate);
        newDetails.setEndTime(endDate);

        when(reservationRepository.findById(1)).thenReturn(Optional.ofNullable(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        //act
        Reservation updatedReservation = reservationService.updateReservation(1, newDetails);

        //assert
        assertNotNull(updatedReservation);
        assertEquals(2, updatedReservation.getUserId());
        verify(reservationRepository, times(1)).findById(1);
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void updateReservation_invalidId() {
        //arrange
        when(reservationRepository.findById(1)).thenReturn(Optional.empty());

        //act
        ReservationDoesNotExistException exception = assertThrows(ReservationDoesNotExistException.class, () -> {
            reservationService.updateReservation(1, reservation);
        });

        //assert
        assertEquals("Reservation not found with id 1", exception.getMessage());
        verify(reservationRepository, times(1)).findById(1);
    }

    @Test
    void deleteReservation_validId() throws ReservationDoesNotExistException {
        //arrange
        when(reservationRepository.existsById(1)).thenReturn(true);

        //act
        reservationService.deleteReservation(1);

        //assert
        verify(reservationRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteReservation_invalidId() {
        //arrange
        when(reservationRepository.existsById(1)).thenReturn(false);

        //act
        ReservationDoesNotExistException exception = assertThrows(ReservationDoesNotExistException.class, () -> {
            reservationService.deleteReservation(1);
        });

        //assert
        assertEquals("Reservation not found with id 1", exception.getMessage());
        verify(reservationRepository, times(1)).existsById(1);
    }
}