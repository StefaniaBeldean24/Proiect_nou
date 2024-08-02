package com.internship.backend.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
class TennisCourtTest {

    private TennisCourt tennisCourt;
    private Location location;
    private Price price;

    @BeforeEach
    void setUp() {
        location = createDefaultLocation();
        price = createDefaultPrice();
        tennisCourt = createDefaultTennisCourt();
    }

    private Location createDefaultLocation() {
        return Location.builder()
                .id(1)
                .name("location")
                .details("details")
                .build();
    }

    private Price createDefaultPrice() {
        return Price.builder()
                .id(1)
                .season("summer")
                .periodOfDay("morning")
                .price(100)
                .build();
    }

    private TennisCourt createDefaultTennisCourt() {
        return TennisCourt.builder()
                .id(1)
                .name("tennisCourt")
                .details("details")
                .location(location)
                .prices(List.of(price))
                .build();
    }

    @AfterEach
    void tearDown() {
        tennisCourt = null;
        location = null;
        price = null;
    }

    @Test
    void getId() {
        assertEquals(1, tennisCourt.getId());
    }

    @Test
    void getName() {
        assertEquals("tennisCourt", tennisCourt.getName());
    }

    @Test
    void getDetails() {
        assertEquals("details", tennisCourt.getDetails());
    }

    @Test
    void getLocation() {
        assertEquals(location, tennisCourt.getLocation());
    }

    @Test
    void getPrices() {
        List<Price> expectedPrice = List.of(price);
        assertEquals(expectedPrice, tennisCourt.getPrices());
    }

    @Test
    void setId() {
        tennisCourt.setId(6);
        assertEquals(6, tennisCourt.getId());
    }

    @Test
    void setName() {
        tennisCourt.setName("newName");
        assertEquals("newName", tennisCourt.getName());
    }

    @Test
    void setDetails() {
        tennisCourt.setDetails("newDetails");
        assertEquals("newDetails", tennisCourt.getDetails());
    }

    @Test
    void setLocation() {
        Location newLocation = Location.builder()
                .id(10)
                .name("newName")
                .details("newDetails")
                .build();
        tennisCourt.setLocation(newLocation);

        assertEquals(newLocation, tennisCourt.getLocation());
    }

    @Test
    void setPrices() {
        Price newPrice = Price.builder()
                .id(11)
                .season("autumn")
                .periodOfDay("afternoon")
                .price(150)
                .build();
        tennisCourt.setPrices(List.of(newPrice));
        List<Price> expectedPrice = List.of(newPrice);
        assertEquals(expectedPrice, tennisCourt.getPrices());
    }
}