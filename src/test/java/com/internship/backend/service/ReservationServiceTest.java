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
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.List.of;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
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
    }

    private User createDefaultUser() {
        return User.builder()
                .id(1)
                .username("testUser")
                .email("test@example.com")
                .build();
    }

    private TennisCourt createDefaultTennisCourt() {
        return TennisCourt.builder()
                .id(1)
                .name("tennisCourt")
                .details("details")
                .build();
    }

    private Reservation createDefaultReservation() {
        return Reservation.builder()
                .id(1)
                .startTime(LocalDateTime.now().plusHours(1))
                .endTime(LocalDateTime.now().plusHours(3))
                .tennisCourt(tennisCourt)
                .user(user)
                .build();
    }

    private ReservationDTO createDefaultReservationDTO() {
        return new ReservationDTO(1, 1, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(3));
    }

    @Test
    void shouldRetrieveAllReservations() {
        reservation = createDefaultReservation();
        reservations = of(reservation);

        when(reservationRepository.findAll()).thenReturn(reservations);
        List<Reservation> result = reservationService.getAllReservations();

        assertEquals(1, result.size());
        assertEquals(reservation, result.get(0));
    }

    @Test
    void shouldAddReservation() throws ReservationAlreadyExists, InvalidDateException, TennisCourtDoesNotExistsException, UserDoesNotExistException {
        user = createDefaultUser();
        tennisCourt = createDefaultTennisCourt();
        reservation = createDefaultReservation();
        reservationDTO = createDefaultReservationDTO();

        when(reservationMapper.mapToReservation(reservationDTO, user, tennisCourt)).thenReturn(reservation);
        when(userRepository.findById(reservationDTO.getUserId())).thenReturn(ofNullable(user));
        when(tennisCourtRepository.findById(reservationDTO.getTennisCourtId())).thenReturn(ofNullable(tennisCourt));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation savedReservation = reservationService.addReservation(reservationDTO);

        assertEquals(reservation.getId(), savedReservation.getId());
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void shouldNotAddReservationWhenUserNotFound() {
        reservationDTO = createDefaultReservationDTO();

        when(userRepository.findById(reservationDTO.getUserId())).thenReturn(empty());

        assertThrows(UserDoesNotExistException.class, () -> reservationService.addReservation(reservationDTO));
    }

    @Test
    void shouldNotAddReservationWhenTennisCourtNotFound() {
        user = createDefaultUser();
        reservationDTO = createDefaultReservationDTO();

        when(userRepository.findById(reservationDTO.getUserId())).thenReturn(Optional.of(user));
        when(tennisCourtRepository.findById(reservationDTO.getTennisCourtId())).thenReturn(empty());

        assertThrows(TennisCourtDoesNotExistsException.class, () -> reservationService.addReservation(reservationDTO));
    }

    @Test
    void shouldUpdateReservation() throws ReservationAlreadyExists, TennisCourtDoesNotExistsException, UserDoesNotExistException {
        user = createDefaultUser();
        tennisCourt = createDefaultTennisCourt();
        reservation = createDefaultReservation();
        reservationDTO = createDefaultReservationDTO();

        when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));
        when(reservationMapper.mapToReservation(reservationDTO, user, tennisCourt)).thenReturn(reservation);
        when(userRepository.findById(reservationDTO.getUserId())).thenReturn(ofNullable(user));
        when(tennisCourtRepository.findById(reservationDTO.getTennisCourtId())).thenReturn(ofNullable(tennisCourt));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation updatedReservation = reservationService.update(reservation.getId(), reservationDTO);

        assertNotNull(updatedReservation);
        assertEquals(reservation.getId(), updatedReservation.getId());
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void shouldNotUpdateWhenUserNotFound() {
        reservation = createDefaultReservation();
        reservationDTO = createDefaultReservationDTO();
        when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));
        when(userRepository.findById(1)).thenReturn(empty());

        assertThrows(UserDoesNotExistException.class, () -> reservationService.update(1, reservationDTO));
    }

    @Test
    void shouldDeleteReservation() throws ReservationDoesNotExistException {
        when(reservationRepository.existsById(1)).thenReturn(true);

        reservationService.delete(1);

        verify(reservationRepository).deleteById(1);
        verify(priceRepository).deleteById(1);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentReservation() {
        reservation = createDefaultReservation();
        when(reservationRepository.existsById(1)).thenReturn(false);

        assertThrows(ReservationDoesNotExistException.class, () -> reservationService.delete(reservation.getId()));
    }

    @Test
    void shouldThrowExceptionWhenReservationNotFound() {
        when(reservationRepository.existsById(1)).thenReturn(false);

        assertThrows(ReservationDoesNotExistException.class, () -> reservationService.delete(1));
    }

    @Test
    void shouldRetrieveAvailableTennisCourts() throws TennisCourtDoesNotExistsException, InvalidDateException {
        tennisCourt = createDefaultTennisCourt();
        reservation = createDefaultReservation();
        reservations = of(reservation);

        when(tennisCourtRepository.findAll()).thenReturn(of(tennisCourt));
        when(reservationRepository.findAll()).thenReturn(reservations);

        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = startDate.plusHours(2);

        List<TennisCourt> availableCourts = reservationService.getAvailableTennisCourts(startDate, endDate);

        assertFalse(availableCourts.isEmpty());
        assertEquals(tennisCourt.getId(), availableCourts.get(0).getId());
    }

    @Test
    void shouldThrowExceptionWhenNoTennisCourtIsAvailable() {
        reservation = createDefaultReservation();
        reservations = of(reservation);

        when(tennisCourtRepository.findAll()).thenReturn(new ArrayList<>());
        when(reservationRepository.findAll()).thenReturn(reservations);

        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = startDate.plusHours(2);

        assertThrows(TennisCourtDoesNotExistsException.class, () -> reservationService.getAvailableTennisCourts(startDate, endDate));
    }
}