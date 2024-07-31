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
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static java.util.Optional.empty;
import static java.util.List.of;
import static java.util.Optional.ofNullable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertThrows;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

@ActiveProfiles("test")
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
        location = createDefaultLocation();
        locationDTO = createDefaultLocationDTO();
        locations = of(location);
    }

    private Location createDefaultLocation() {
        return Location.builder()
                .id(1)
                .name("Location")
                .details("Details about Location")
                .build();
    }

    private LocationDTO createDefaultLocationDTO() {
        return new LocationDTO("Location1", "Details");
    }

    @Test
    void shouldRetrieveAllLocations() {
        when(locationRepository.findAll()).thenReturn(locations);

        List<Location> result = locationService.getAllLocations();

        assertEquals(1, result.size());
        assertEquals(location, result.get(0));
    }

    @Test
    void shouldAddLocation() throws LocationAlreadyExistsException {
        when(locationMapper.mapToLocation(locationDTO)).thenReturn(location);
        when(locationRepository.existsByName(location.getName())).thenReturn(false);
        when(locationRepository.save(any(Location.class))).thenReturn(location);

        Location savedLocation = locationService.addLocation(locationDTO);

        assertNotNull(savedLocation); //The returned location should not be null
        assertEquals(location.getName(), savedLocation.getName()); //savedLocation should have the same name as location
        verify(locationRepository).save(location);
    }

    @Test
    void shouldThrowExceptionWhenAddingAnExisitngLocation() {
        when(locationMapper.mapToLocation(locationDTO)).thenReturn(location);
        when(locationRepository.existsByName(location.getName())).thenReturn(true); //if my location already exists by name, it will throw exception

        assertThrows(LocationAlreadyExistsException.class, () -> locationService.addLocation(locationDTO));

        verify(locationRepository, never()).save(any(Location.class)); //verify that the save method was never called
    }

    @Test
    void shouldUpdateExistingLocation() throws LocationDoesNotExistException {
        when(locationRepository.findById(1)).thenReturn(ofNullable(location));
        when(locationRepository.save(any(Location.class))).thenReturn(location);

        LocationDTO updatedLocationDTO = new LocationDTO("UpdatedLocation", "Updated details about Location");
        Location updatedLocation = locationService.update(1, updatedLocationDTO);

        assertEquals(updatedLocationDTO.getName(), updatedLocation.getName()); //updatedLocationDTO should have the same name as updatedLocation
        assertEquals(updatedLocationDTO.getDetails(), updatedLocation.getDetails()); ////updatedLocationDTO should have the same details as updatedLocation
        verify(locationRepository).save(any(Location.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentLocation() {
        when(locationRepository.findById(1)).thenReturn(empty());

        LocationDTO updatedLocationDTO = new LocationDTO("UpdatedLocation", "Updated details about Location");

        assertThrows(LocationDoesNotExistException.class, () -> locationService.update(1, updatedLocationDTO));
    }

    @Test
    void shouldDeleteExistingLocation() throws LocationDoesNotExistException {
        when(locationRepository.existsById(1)).thenReturn(true); //check if location with id 1 exists

        locationService.delete(1);

        verify(locationRepository).deleteById(1); //verify that deleteById method was called once
    }

    @Test
    void shouldThrowExceptionWhenLocationNotFoundToDelete() {
        when(locationRepository.existsById(1)).thenReturn(false);

        assertThrows(LocationDoesNotExistException.class, () -> locationService.delete(1));

        verify(locationRepository, never()).save(any(Location.class));
    }
}