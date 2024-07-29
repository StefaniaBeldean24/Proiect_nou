package com.internship.backend.controller;
import com.internship.backend.dto.LocationDTO;
import com.internship.backend.exceptions.LocationAlreadyExistsException;
import com.internship.backend.exceptions.LocationDoesNotExistException;
import com.internship.backend.model.Location;
import com.internship.backend.service.LocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class LocationControllerTest {

    @InjectMocks
    private LocationController locationController;

    @Mock
    private LocationService locationService;

    private MockMvc mockMvc;
    private Location location;
    private List<Location> locations;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(locationController).build();

        location = Location.builder()
                .id(1)
                .name("Location")
                .details("Details")
                .build();

        locations = List.of(location);
    }

    @Test
    void addLocation() throws Exception {
        when(locationService.addLocation(any(LocationDTO.class))).thenReturn(location);

        mockMvc.perform(post("/api/locations/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Location\",\"details\":\"Details\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(location.getName())))
                .andExpect(jsonPath("$.details", is(location.getDetails())));

        verify(locationService).addLocation(any(LocationDTO.class));
    }

    @Test
    void addLocationThrowsConflict() throws Exception {
        when(locationService.addLocation(any(LocationDTO.class))).thenThrow(LocationAlreadyExistsException.class);

        mockMvc.perform(post("/api/locations/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Location\",\"details\":\"Details\"}"))
                .andExpect(status().isConflict());

        verify(locationService).addLocation(any(LocationDTO.class));
    }

    @Test
    void getAllLocations() throws Exception {
        when(locationService.getAllLocations()).thenReturn(locations);

        mockMvc.perform(get("/api/locations/getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(location.getName())))
                .andExpect(jsonPath("$[0].details", is(location.getDetails())));

        verify(locationService).getAllLocations();
    }

    @Test
    void updateLocation() throws Exception {
        when(locationService.update(anyInt(), any(LocationDTO.class))).thenReturn(location);

        mockMvc.perform(put("/api/locations/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"updatedLocation\",\"details\":\"updatedDetails\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(location.getName())))
                .andExpect(jsonPath("$.details", is(location.getDetails())));

        verify(locationService).update(anyInt(), any(LocationDTO.class));
    }

    @Test
    void updateLocationThrowsNotFound() throws Exception {
        when(locationService.update(anyInt(), any(LocationDTO.class))).thenThrow(LocationDoesNotExistException.class);

        mockMvc.perform(put("/api/locations/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"updatedLocation\",\"details\":\"updatedDetails\"}"))
                .andExpect(status().isNotFound());

        verify(locationService).update(anyInt(), any(LocationDTO.class));
    }

    @Test
    void deleteLocation() throws Exception {
        doNothing().when(locationService).delete(anyInt());

        mockMvc.perform(delete("/api/locations/delete/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(locationService).delete(anyInt());
    }

    @Test
    void deleteLocationThrowsNotFound() throws Exception {
        doThrow(LocationDoesNotExistException.class).when(locationService).delete(anyInt());

        mockMvc.perform(delete("/api/locations/delete/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(locationService).delete(anyInt());
    }
}