package com.internship.backend.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsersTest {

    private Users user1;
    private Users user2;

    @BeforeEach
    void setUp() {
        user1 = new Users(1, "username1", "password", "email");
        user2 = new Users();
    }

    @Test
    void testEquals() {
        Users user3 = new Users(1, "username1", "password", "email");
        Users user4 = new Users(3, "username1", "password", "email");

        assertEquals(user1, user3);
        assertNotEquals(user1, user4);
        assertNotEquals(user1, null);
        assertNotEquals(user1, new Object());
    }

    @Test
    void testHashCode() {
        Users user3 = new Users(1, "username1", "password", "email");
        assertEquals(user1.hashCode(), user3.hashCode());
    }



    @Test
    void getId() {
        assertEquals(user1.getId(), 1);
    }

    @Test
    void getUsername() {
        assertEquals(user1.getUsername(), "username1");
    }

    @Test
    void getPassword() {
        assertEquals(user1.getPassword(), "password");
    }

    @Test
    void getEmail() {
        assertEquals(user1.getEmail(), "email");
    }

    @Test
    void setId() {
        user2.setId(2);
        assertEquals(user2.getId(), 2);
    }

    @Test
    void setUsername() {
        user2.setUsername("username2");
        assertEquals(user2.getUsername(), "username2");
    }

    @Test
    void setPassword() {
        user2.setPassword("password2");
        assertEquals(user2.getPassword(), "password2");
    }

    @Test
    void setEmail() {
        user2.setEmail("email2");
        assertEquals(user2.getEmail(), "email2");
    }
}