package com.internship.backend.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
class LocationTest {

    private Location location;
    private TennisCourt tennisCourt;

    @BeforeEach
    void setUp() {
        tennisCourt = createDefaultTennisCourt();
        location = createDefaultLocation();
    }

    public TennisCourt createDefaultTennisCourt() {
        return TennisCourt.builder()
                .id(1)
                .name("tennisCourt")
                .details("details")
                .build();
    }

    public Location createDefaultLocation() {
        return Location.builder()
                .id(1)
                .name("location")
                .details("details")
                .tennisCourt(List.of(tennisCourt))
                .build();
    }

    @AfterEach
    void tearDown() {
        location = null;
        tennisCourt = null;
    }

    @Test
    void testToString() {
        String expectedString = "Location{id=1, name='location', details='details', tennisCourt=[" + tennisCourt + "]}";
        assertEquals(expectedString, location.toString());
    }

    @Test
    void getId() {
        assertEquals(1, location.getId());
    }

    @Test
    void getName() {
        assertEquals("location", location.getName());
    }

    @Test
    void getDetails() {
        assertEquals("details", location.getDetails());
    }

    @Test
    void getTennisCourt() {

        assertEquals(List.of(tennisCourt), location.getTennisCourt());
    }

    @Test
    void setId() {
        location.setId(3);
        assertEquals(3, location.getId());
    }

    @Test
    void setName() {
        location.setName("newLocation");
        assertEquals("newLocation", location.getName());
    }

    @Test
    void setDetails() {
        location.setDetails("newDetails");
        assertEquals("newDetails", location.getDetails());
    }

    @Test
    void setTennisCourt() {
        TennisCourt tennisCourt2 = tennisCourt.builder()
                .id(2)
                .name("tennisCourt2")
                .details("details")
                .build();
        location.setTennisCourt(List.of(tennisCourt2));

        assertEquals(tennisCourt2, location.getTennisCourt().get(0));
    }
}