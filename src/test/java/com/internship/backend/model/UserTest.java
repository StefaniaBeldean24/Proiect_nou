package com.internship.backend.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class UserTest {

    private Validator validator;
    private User validUser;
    private User invalidUser;
    private Set<Authority> authorities;
    private List<Reservation> reservations;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        reservations = new ArrayList<>();

        validUser = User.builder()
                .id(1)
                .username("user1")
                .password("password1")
                .email("user1@email.com")
                .build();

        invalidUser = User.builder()
                .id(2)
                .username("us")
                .password("p")
                .email("u@.c")
                .build();
    }

    @AfterEach
    void tearDown() {
        validUser = null;
        invalidUser = null;
        authorities = null;
        reservations = null;
    }

    @Test
    void testUserValidatons(){
        Set<ConstraintViolation<User>> violations = validator.validate(validUser);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testEquals() {
        User user3 = User.builder()
                .id(1)
                .username("user1")
                .password("password1")
                .email("user1@email.com")
                .build();

        assertEquals(validUser, user3);
    }

    @Test
    void testHashCode() {
        User user3 = User.builder()
                .id(1)
                .username("user1")
                .password("password1")
                .email("user1@email.com")
                .build();

        assertEquals(validUser.hashCode(), user3.hashCode());
    }

    @Test
    void testToString() {
        String expected1 = "User{id=1, username='user1', password='password1', email='user1@email.com'}";

        assertEquals(expected1, validUser.toString());
    }

    @Test
    void getId() {
        assertEquals(1, validUser.getId());
        assertEquals(2, invalidUser.getId());
    }

    @Test
    void getUsername() {
        assertEquals("user1", validUser.getUsername());
    }

    @Test
    void invalidUsernameValidaton() {
        Set<ConstraintViolation<User>> violations = validator.validate(invalidUser);
        boolean usernameErrorFound = false;
        for(ConstraintViolation<User> violation : violations){
            if("username must be between 4 and 10 characters".equals(violation.getMessage())){
                usernameErrorFound = true;
                break;
            }
        }
        assertTrue(usernameErrorFound);
    }

    @Test
    void getPassword() {
        assertEquals("password1", validUser.getPassword());
    }

    @Test
    void invalidPasswordValidaton() {
        Set<ConstraintViolation<User>> violations = validator.validate(invalidUser);
        boolean passwordErrorFound = false;
        for(ConstraintViolation<User> violation : violations){
            if("password must be between 8 and 30 characters".equals(violation.getMessage())){
                passwordErrorFound = true;
                break;
            }
        }
       assertTrue(passwordErrorFound);
    }

    @Test
    void getEmail() {
        assertEquals("user1@email.com", validUser.getEmail());
    }

    @Test
    void invalidEmailValidaton() {
        Set<ConstraintViolation<User>> violations = validator.validate(invalidUser);
        boolean emailErrorFound = false;
        for(ConstraintViolation<User> violation : violations){
            if("email must be between 6 and 30 characters".equals(violation.getMessage())){
                emailErrorFound = true;
                break;
            }
        }
       assertTrue(emailErrorFound);
    }

    @Test
    void getAuthorities() {
        Authority authority = Authority.builder()
                .id(1)
                .name("ROLE_ADMIN")
                .build();

        authorities = Set.of(authority);
        validUser.setAuthorities(authorities);
        assertEquals(authorities, validUser.getAuthorities());
    }

    @Test
    void getReservations() {
        Authority authority = Authority.builder()
                .id(1)
                .name("ROLE_ADMIN")
                .build();

        authorities = Set.of(authority);
        validUser.setAuthorities(authorities);

        Location location = Location.builder()
                .name("location1")
                .details("details")
                .build();

        TennisCourt tennisCourt = TennisCourt.builder()
                .name("tenniscourt1")
                .details("details")
                .location(location)
                .build();

        location.setTennisCourt(List.of(tennisCourt));

        Reservation reservation = Reservation.builder()
                .startTime(LocalDateTime.now().plusHours(12))
                .endTime(LocalDateTime.now().plusHours(14))
                .tennisCourt(tennisCourt)
                .build();

        reservations.add(reservation);
        validUser.setReservations(reservations);
        assertEquals(reservations, validUser.getReservations());
    }

    @Test
    void setId() {
        validUser.setId(3);
        assertEquals(3, validUser.getId());
    }

    @Test
    void setUsername() {
        validUser.setUsername("username");
        assertEquals("username", validUser.getUsername());
    }

    @Test
    void setPassword() {
        validUser.setPassword("password");
        assertEquals("password", validUser.getPassword());
    }

    @Test
    void setEmail() {
        validUser.setEmail("email@yahoo.com");
        assertEquals("email@yahoo.com", validUser.getEmail());
    }

    @Test
    void setAuthorities() {
        Authority authority = Authority.builder()
                .id(1)
                .name("ROLE_ADMIN")
                .build();

        authority.setName("ROLE_USER");
        assertEquals("ROLE_USER", authority.getName());
    }

    @Test
    void setReservations() {
        Location location = Location.builder()
                .name("location1")
                .details("details")
                .build();

        TennisCourt tennisCourt1 = TennisCourt.builder()
                .name("tenniscourt1")
                .details("details")
                .location(location)
                .build();

        TennisCourt tennisCourt2 = TennisCourt.builder()
                .name("tenniscourt2")
                .details("details")
                .location(location)
                .build();

        location.setTennisCourt(List.of(tennisCourt1));

        Reservation reservation = Reservation.builder()
                .startTime(LocalDateTime.now().plusHours(12))
                .endTime(LocalDateTime.now().plusHours(14))
                .tennisCourt(tennisCourt1)
                .build();

        reservation.setTennisCourt(tennisCourt2);
        reservations.add(reservation);
        assertEquals(reservations.get(0).getTennisCourt(), tennisCourt2);
    }
}