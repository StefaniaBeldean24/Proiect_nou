package com.internship.backend.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class PriceTest {

    private Price price;
    private TennisCourt tennisCourt;
    private Location location;

    @BeforeEach
    void setUp() {
        location = Location.builder()
                .id(1)
                .name("location")
                .details("details")
                .build();

        tennisCourt = TennisCourt.builder()
                .name("tennisCourt")
                .location(location)
                .build();

        location.setTennisCourt(List.of(tennisCourt));

        price = Price.builder()
                .id(1)
                .season("summer")
                .periodOfDay("morning")
                .price(100)
                .tennisCourt(tennisCourt)
                .build();
    }

    @AfterEach
    void tearDown() {
        price = null;
        tennisCourt = null;
        location = null;
    }

    @Test
    void getId() {
        assertEquals(1, price.getId());
    }

    @Test
    void getSeason() {
        assertEquals("summer", price.getSeason());
    }

    @Test
    void getPeriodOfDay() {
        assertEquals("morning", price.getPeriodOfDay());
    }

    @Test
    void getPrice() {
        assertEquals(100, price.getPrice());
    }

    @Test
    void getTennisCourt() {
        assertEquals(tennisCourt, price.getTennisCourt());
    }

    @Test
    void setId() {
        price.setId(5);
        assertEquals(5, price.getId());
    }

    @Test
    void setSeason() {
        price.setSeason("autumn");
        assertEquals("autumn", price.getSeason());
    }

    @Test
    void setPeriodOfDay() {
        price.setPeriodOfDay("afternoon");
        assertEquals("afternoon", price.getPeriodOfDay());
    }

    @Test
    void setPrice() {
        price.setPrice(200);
        assertEquals(200, price.getPrice());
    }

    @Test
    void setTennisCourt() {
        TennisCourt tennisCourt1 = TennisCourt.builder()
                .name("tennisCourt1")
                .location(location)
                .build();

        price.setTennisCourt(tennisCourt1);
        assertEquals(tennisCourt1, price.getTennisCourt());
    }
}