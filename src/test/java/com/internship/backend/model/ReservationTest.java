package com.internship.backend.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
class ReservationTest {

    private User user;
    private TennisCourt tennisCourt;
    private Reservation reservation;

    @BeforeEach
    void setUp() {
        user = createDefaultUser();
        tennisCourt = createDefaultTennisCourt();
        reservation = createDefaultReservation();
    }

    private User createDefaultUser() {
        return User.builder()
                .id(1)
                .username("test")
                .password("password")
                .email("email@yahoo.com")
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
                .startTime(LocalDateTime.now().plusHours(2))
                .endTime(LocalDateTime.now().plusHours(4))
                .tennisCourt(tennisCourt)
                .user(user)
                .build();
    }

    @AfterEach
    void tearDown() {
        user = null;
        tennisCourt = null;
        reservation = null;
    }

    @Test
    void getId() {
        assertEquals(1, reservation.getId());
    }

    @Test
    void getStartTime() {
        assertEquals(reservation.getStartTime(), reservation.getStartTime());
    }

    @Test
    void getEndTime() {
        assertEquals(reservation.getEndTime(), reservation.getEndTime());
    }

    @Test
    void getUser() {
        assertEquals(user, reservation.getUser());
    }

    @Test
    void getTennisCourt() {
        assertEquals(tennisCourt, reservation.getTennisCourt());
    }

    @Test
    void setId() {
        reservation.setId(3);
        assertEquals(3, reservation.getId());
    }

    @Test
    void setStartTime() {
        reservation.setStartTime(LocalDateTime.of(2024, 7, 28, 15, 0));
        assertEquals(LocalDateTime.of(2024, 7, 28, 15, 0), reservation.getStartTime());
    }

    @Test
    void setEndTime() {
        reservation.setEndTime(LocalDateTime.of(2024, 7, 28, 15, 0));
        assertEquals(LocalDateTime.of(2024, 7, 28, 15, 0), reservation.getEndTime());
    }

    @Test
    void setUser() {
        User user2 = User.builder()
                .id(5)
                .username("test1")
                .password("password1")
                .email("email@yahoo.com")
                .build();
        reservation.setUser(user2);

        assertEquals(user2, reservation.getUser());
    }

    @Test
    void setTennisCourt() {
        TennisCourt tennisCourt2 = tennisCourt.builder()
                .id(10)
                .name("tennisCourt2")
                .details("details2")
                .build();
        reservation.setTennisCourt(tennisCourt2);

        assertEquals(tennisCourt2, reservation.getTennisCourt());
    }
}