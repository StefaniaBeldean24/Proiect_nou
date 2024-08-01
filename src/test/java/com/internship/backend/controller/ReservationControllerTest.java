package com.internship.backend.controller;

import com.internship.backend.dto.ReservationDTO;
import com.internship.backend.exceptions.ReservationAlreadyExists;
import com.internship.backend.exceptions.ReservationDoesNotExistException;
import com.internship.backend.exceptions.TennisCourtDoesNotExistsException;
import com.internship.backend.model.Reservation;
import com.internship.backend.model.TennisCourt;
import com.internship.backend.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
class ReservationControllerTest {

    @InjectMocks
    private ReservationController reservationController;

    @Mock
    private ReservationService reservationService;

    private MockMvc mockMvc;
    private Reservation reservation;
    private ReservationDTO reservationDTO;
    private TennisCourt tennisCourt;
    private List<Reservation> reservations;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(reservationController).build();
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
                .build();
    }

    private ReservationDTO createDefaultReservationDTO() {
        return new ReservationDTO(1, 1, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(3));
    }

    @Test
    void shouldAddReservation() throws Exception {
        reservationDTO = createDefaultReservationDTO();
        tennisCourt = createDefaultTennisCourt();
        reservation = createDefaultReservation();
        when(reservationService.addReservation(any(ReservationDTO.class))).thenReturn(reservation);

        mockMvc.perform(post("/api/reservations/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"tennisCourtId\":1,\"startTime\":\"" + reservationDTO.getStartTime() + "\",\"endTime\":\"" + reservationDTO.getEndTime() + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tennisCourt.name", is(tennisCourt.getName())))
                .andExpect(jsonPath("$.tennisCourt.details", is(tennisCourt.getDetails())));

        verify(reservationService).addReservation(any(ReservationDTO.class));
    }

    @Test
    void shouldNotAddAlreadyExistingReservation() throws Exception {
        reservationDTO = createDefaultReservationDTO();
        reservation = createDefaultReservation();
        when(reservationService.addReservation(any(ReservationDTO.class))).thenThrow(ReservationAlreadyExists.class);

        mockMvc.perform(post("/api/reservations/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"tennisCourtId\":1,\"startTime\":\"" + reservationDTO.getStartTime() + "\",\"endTime\":\"" + reservationDTO.getEndTime() + "\"}"))
                .andExpect(status().isBadRequest());

        verify(reservationService).addReservation(any(ReservationDTO.class));
    }

    @Test
    void shouldRetrieveAllReservations() throws Exception {
        tennisCourt = createDefaultTennisCourt();
        reservation = createDefaultReservation();
        reservations = List.of(reservation);
        when(reservationService.getAllReservations()).thenReturn(reservations);

        mockMvc.perform(get("/api/reservations/getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].tennisCourt.name", is(tennisCourt.getName())))
                .andExpect(jsonPath("$[0].tennisCourt.details", is(tennisCourt.getDetails())));

        verify(reservationService).getAllReservations();
    }

    @Test
    void shouldRetrieveAvailableTennisCourts() throws Exception {
        reservationDTO = createDefaultReservationDTO();
        tennisCourt = createDefaultTennisCourt();
        reservation = createDefaultReservation();
        reservations = List.of(reservation);
        List<TennisCourt> tennisCourts = List.of(tennisCourt);
        when(reservationService.getAvailableTennisCourts(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(tennisCourts);

        mockMvc.perform(post("/api/reservations/available")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"start\":\"" + reservationDTO.getStartTime() + "\",\"end\":\"" + reservationDTO.getEndTime() + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(tennisCourt.getName())))
                .andExpect(jsonPath("$[0].details", is(tennisCourt.getDetails())));

        verify(reservationService).getAvailableTennisCourts(any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void shouldThrowErrorWhenTennisCourtNotFound() throws Exception {
        reservationDTO = createDefaultReservationDTO();
        when(reservationService.getAvailableTennisCourts(any(LocalDateTime.class), any(LocalDateTime.class))).thenThrow(TennisCourtDoesNotExistsException.class);

        mockMvc.perform(post("/api/reservations/available")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"start\":\"" + reservationDTO.getStartTime() + "\",\"end\":\"" + reservationDTO.getEndTime() + "\"}"))
                .andExpect(status().isNotFound());

        verify(reservationService).getAvailableTennisCourts(any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void shouldUpdateReservation() throws Exception {
        tennisCourt = createDefaultTennisCourt();
        reservation = createDefaultReservation();
        reservationDTO = createDefaultReservationDTO();

        when(reservationService.update(anyInt(), any(ReservationDTO.class))).thenReturn(reservation);

        mockMvc.perform(put("/api/reservations/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"tennisCourtId\":1,\"startTime\":\"" + reservationDTO.getStartTime() + "\",\"endTime\":\"" + reservationDTO.getEndTime() + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tennisCourt.name", is(tennisCourt.getName())))
                .andExpect(jsonPath("$.tennisCourt.details", is(tennisCourt.getDetails())));

        verify(reservationService).update(anyInt(), any(ReservationDTO.class));
    }

    @Test
    void shouldNotProcessUpdatingOperation() throws Exception {
        reservationDTO = createDefaultReservationDTO();
        when(reservationService.update(anyInt(), any(ReservationDTO.class))).thenThrow(ReservationAlreadyExists.class);

        mockMvc.perform(put("/api/reservations/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"tennisCourtId\":1,\"startTime\":\"" + reservationDTO.getStartTime() + "\",\"endTime\":\"" + reservationDTO.getEndTime() + "\"}"))
                .andExpect(status().isNotFound());

        verify(reservationService).update(anyInt(), any(ReservationDTO.class));
    }

    @Test
    void shouldDeleteReservation() throws Exception {
        doNothing().when(reservationService).delete(anyInt());

        mockMvc.perform(delete("/api/reservations/delete/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(reservationService).delete(anyInt());
    }

    @Test
    void shouldNotDeleteNonExistingReservation() throws Exception {
        doThrow(ReservationDoesNotExistException.class).when(reservationService).delete(anyInt());

        mockMvc.perform(delete("/api/reservations/delete/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(reservationService).delete(anyInt());
    }
}