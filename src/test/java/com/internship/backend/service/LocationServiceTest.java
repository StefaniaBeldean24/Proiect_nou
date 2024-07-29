package com.internship.backend.service;

import com.internship.backend.dto.LocationDTO;
import com.internship.backend.exceptions.LocationAlreadyExistsException;
import com.internship.backend.exceptions.LocationDoesNotExistException;
import com.internship.backend.mappper.LocationMapper;
import com.internship.backend.model.Location;
import com.internship.backend.repository.LocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertThrows;
import static org.testng.AssertJUnit.assertEquals;


class LocationServiceTest {

    @InjectMocks
    private LocationService locationService;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private LocationMapper locationMapper;

    private Location location;
    private LocationDTO locationDTO;
    private List<Location> locations;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        location = Location.builder()
                .id(1)
                .name("Location1")
                .details("Details about Location1")
                .build();

        locationDTO = new LocationDTO("Location1", "Details");

        locations = List.of(location);
    }

    @Test
    void getAllLocations() {
        when(locationRepository.findAll()).thenReturn(locations);

        List<Location> result = locationService.getAllLocations();

        assertEquals(1, result.size());
        assertEquals(location, result.get(0));
    }

    @Test
    void addLocation() throws LocationAlreadyExistsException {
        when(locationMapper.locationMapper(any(LocationDTO.class))).thenReturn(location);
        when(locationRepository.existsByName(location.getName())).thenReturn(false);
        when(locationRepository.save(any(Location.class))).thenReturn(location);

        Location savedLocation = locationService.addLocation(locationDTO);

        assertEquals(location.getId(), savedLocation.getId());
        assertEquals(location.getName(), savedLocation.getName());
        assertEquals(location.getDetails(), savedLocation.getDetails());
        verify(locationRepository).save(any(Location.class));
    }

    @Test
    void addLocationThrowsExceptionWhenAlreadyExists() {
        when(locationMapper.locationMapper(any(LocationDTO.class))).thenReturn(location);
        when(locationRepository.existsByName(location.getName())).thenReturn(true);

        assertThrows(LocationAlreadyExistsException.class, () -> locationService.addLocation(locationDTO));
    }

    @Test
    void updateLocation() throws LocationDoesNotExistException {
        when(locationRepository.findById(1)).thenReturn(Optional.of(location));
        when(locationRepository.save(any(Location.class))).thenReturn(location);

        LocationDTO updatedLocationDTO = new LocationDTO("UpdatedLocation", "Updated details about Location");
        Location updatedLocation = locationService.update(1, updatedLocationDTO);

        assertEquals(updatedLocationDTO.getName(), updatedLocation.getName());
        assertEquals(updatedLocationDTO.getDetails(), updatedLocation.getDetails());
        verify(locationRepository).save(any(Location.class));
    }

    @Test
    void updateLocationThrowsExceptionWhenNotFound() {
        when(locationRepository.findById(1)).thenReturn(Optional.empty());

        LocationDTO updatedLocationDTO = new LocationDTO("UpdatedLocation", "Updated details about Location");

        assertThrows(LocationDoesNotExistException.class, () -> locationService.update(1, updatedLocationDTO));
    }

    @Test
    void deleteLocation() throws LocationDoesNotExistException {
        when(locationRepository.existsById(1)).thenReturn(true);

        locationService.delete(1);

        verify(locationRepository).deleteById(1);
    }

    @Test
    void deleteLocationThrowsExceptionWhenNotFound() {
        when(locationRepository.existsById(1)).thenReturn(false);

        assertThrows(LocationDoesNotExistException.class, () -> locationService.delete(1));
    }
}