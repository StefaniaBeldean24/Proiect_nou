package com.internship.backend.service;

import com.internship.backend.dto.ReservationDTO;
import com.internship.backend.exceptions.*;
import com.internship.backend.mappper.ReservationMapper;
import com.internship.backend.model.Reservation;
import com.internship.backend.model.TennisCourt;
import com.internship.backend.model.User;
import com.internship.backend.repository.PriceRepository;
import com.internship.backend.repository.ReservationRepository;
import com.internship.backend.repository.TennisCourtRepository;
import com.internship.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReservationServiceTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TennisCourtRepository tennisCourtRepository;

    @Mock
    private PriceRepository priceRepository;

    @Mock
    private ReservationMapper reservationMapper;

    private Reservation reservation;
    private ReservationDTO reservationDTO;
    private User user;
    private TennisCourt tennisCourt;
    private List<Reservation> reservations;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder()
                .id(1)
                .username("testUser")
                .email("test@example.com")
                .build();

        tennisCourt = TennisCourt.builder()
                .id(1)
                .name("Court1")
                .details("Details")
                .build();

        reservation = Reservation.builder()
                .id(1)
                .startTime(LocalDateTime.now().plusHours(1))
                .endTime(LocalDateTime.now().plusHours(3))
                .user(user)
                .tennisCourt(tennisCourt)
                .build();

        reservationDTO = new ReservationDTO(1, 1, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(3));

        reservations = new ArrayList<>();
        reservations.add(reservation);
    }

    @Test
    void getAllReservations() {
        when(reservationRepository.findAll()).thenReturn(reservations);

        List<Reservation> result = reservationService.getAllReservations();

        assertEquals(1, result.size());
        assertEquals(reservation, result.get(0));
    }

    @Test
    void addReservation() throws ReservationAlreadyExists, InvalidDateException, TennisCourtDoesNotExistsException, UserDoesNotExistException, UserDoesNotExistException {
        when(reservationMapper.reservatonMapper(any(ReservationDTO.class))).thenReturn(reservation);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation savedReservation = reservationService.addReservation(reservationDTO);

        assertEquals(reservation.getId(), savedReservation.getId());
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void addReservationThrowsExceptionWhenInvalid() throws TennisCourtDoesNotExistsException, UserDoesNotExistException {
        when(reservationMapper.reservatonMapper(any(ReservationDTO.class))).thenReturn(reservation);

        reservation.setStartTime(LocalDateTime.now().minusHours(2)); // Invalid start time

        assertThrows(InvalidDateException.class, () -> reservationService.addReservation(reservationDTO));
    }

    @Test
    void updateReservation() throws ReservationAlreadyExists, TennisCourtDoesNotExistsException, UserDoesNotExistException {
        when(reservationRepository.findById(1)).thenReturn(Optional.of(reservation));
        when(reservationMapper.reservatonMapper(any(ReservationDTO.class))).thenReturn(reservation);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        ReservationDTO updatedDTO = new ReservationDTO(1, 1, LocalDateTime.now().plusHours(1).withNano(0), LocalDateTime.now().plusHours(3).withNano(0));
        Reservation updatedReservation = reservationService.update(1, updatedDTO);

        assertEquals(updatedDTO.getStartTime().withNano(0), updatedReservation.getStartTime().withNano(0));
        assertEquals(updatedDTO.getEndTime().withNano(0), updatedReservation.getEndTime().withNano(0));
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void updateReservationThrowsExceptionWhenNotFound() {
        when(reservationRepository.findById(1)).thenReturn(Optional.empty());

        ReservationDTO updatedDTO = new ReservationDTO(1, 1, LocalDateTime.now().plusHours(4), LocalDateTime.now().plusHours(6));

        assertThrows(ReservationAlreadyExists.class, () -> reservationService.update(1, updatedDTO));
    }

    @Test
    void deleteReservation() throws ReservationDoesNotExistException {
        when(reservationRepository.existsById(1)).thenReturn(true);

        reservationService.delete(1);

        verify(reservationRepository).deleteById(1);
        verify(priceRepository).deleteById(1);
    }

    @Test
    void deleteReservationThrowsExceptionWhenNotFound() {
        when(reservationRepository.existsById(1)).thenReturn(false);

        assertThrows(ReservationDoesNotExistException.class, () -> reservationService.delete(1));
    }

    @Test
    void getAvailableTennisCourts() throws TennisCourtDoesNotExistsException, InvalidDateException {
        when(tennisCourtRepository.findAll()).thenReturn(List.of(tennisCourt));
        when(reservationRepository.findAll()).thenReturn(reservations);

        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = startDate.plusHours(2);

        List<TennisCourt> availableCourts = reservationService.getAvailableTennisCourts(startDate, endDate);

        assertFalse(availableCourts.isEmpty());
        assertEquals(tennisCourt.getId(), availableCourts.get(0).getId());
    }

    @Test
    void getAvailableTennisCourtsThrowsExceptionWhenNoneAvailable() {
        when(tennisCourtRepository.findAll()).thenReturn(new ArrayList<>());
        when(reservationRepository.findAll()).thenReturn(reservations);

        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = startDate.plusHours(2);

        assertThrows(TennisCourtDoesNotExistsException.class, () -> reservationService.getAvailableTennisCourts(startDate, endDate));
    }
}