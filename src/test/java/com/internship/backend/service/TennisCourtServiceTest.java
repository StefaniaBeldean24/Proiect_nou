package com.internship.backend.service;

import com.internship.backend.dto.TennisCourtDTO;
import com.internship.backend.exceptions.LocationDoesNotExistException;
import com.internship.backend.exceptions.TennisCourtAlreadyExistsException;
import com.internship.backend.exceptions.TennisCourtDoesNotExistsException;
import com.internship.backend.mappper.TennisCourtMapper;
import com.internship.backend.model.Location;
import com.internship.backend.model.TennisCourt;
import com.internship.backend.repository.LocationRepository;
import com.internship.backend.repository.TennisCourtRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class TennisCourtServiceTest {

    @InjectMocks
    private TennisCourtService tennisCourtService;

    @Mock
    private TennisCourtRepository tennisCourtRepository;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private TennisCourtMapper tennisCourtMapper;

    private TennisCourt tennisCourt;
    private TennisCourtDTO tennisCourtDTO;
    private Location location;
    private List<TennisCourt> tennisCourts;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        location = createDefaultLocation();

        tennisCourt = createDefaultTennisCourt();

        tennisCourtDTO = createDefaultTennisCourtDTO();

        tennisCourts = of(tennisCourt);
    }

    private Location createDefaultLocation() {
        return Location.builder()
                .id(1)
                .name("Location1")
                .tennisCourt(new ArrayList<>())
                .build();
    }

    private TennisCourt createDefaultTennisCourt() {
        return TennisCourt.builder()
                .id(1)
                .name("Court1")
                .details("Details")
                .location(location)
                .build();
    }

    private TennisCourtDTO createDefaultTennisCourtDTO() {
        return new TennisCourtDTO("Court1", "Details", location.getId());
    }

    @Test
    void shouldRetrieveAllTennisCourts() {
        when(tennisCourtRepository.findAll()).thenReturn(tennisCourts);

        List<TennisCourt> result = tennisCourtService.getAllTennisCourts();

        assertEquals(1, result.size());
        assertEquals(tennisCourt, result.get(0));
    }

    @Test
    void addTennisCourt() throws TennisCourtAlreadyExistsException, LocationDoesNotExistException {
        when(tennisCourtMapper.mapToTennisCourt(tennisCourtDTO, location)).thenReturn(tennisCourt);
        when(locationRepository.findAll()).thenReturn((of(location)));
        when(locationRepository.findById(tennisCourtDTO.getLocationId())).thenReturn(Optional.of(location));
        when(tennisCourtRepository.save(tennisCourt)).thenReturn(tennisCourt);

        TennisCourt savedTennisCourt = tennisCourtService.addTennisCourt(tennisCourtDTO);

        assertNotNull(savedTennisCourt);
        assertEquals(tennisCourt, savedTennisCourt);
        verify(tennisCourtRepository).save(any(TennisCourt.class));
    }

    @Test
    void shouldThrowExceptionWhenAddingTennisCourtToNonExistingLocation() {
        when(locationRepository.findById(tennisCourtDTO.getLocationId())).thenReturn(empty());

        assertThrows(LocationDoesNotExistException.class, () -> tennisCourtService.addTennisCourt(tennisCourtDTO));

        verify(tennisCourtRepository, never()).save(tennisCourt);
    }

    @Test
    void updateTennisCourt() throws TennisCourtDoesNotExistsException {
        when(tennisCourtRepository.findById(1)).thenReturn(Optional.of(tennisCourt));
        when(tennisCourtRepository.save(any(TennisCourt.class))).thenReturn(tennisCourt);

        TennisCourtDTO newTennisCourtDTO = new TennisCourtDTO("UpdatedCourt", "UpdatedDetails", location.getId());
        TennisCourt updatedTennisCourt = tennisCourtService.updateTennisCourt(1, newTennisCourtDTO);

        assertEquals("UpdatedCourt", updatedTennisCourt.getName());
        assertEquals("UpdatedDetails", updatedTennisCourt.getDetails());
    }

    @Test
    void updateTennisCourtThrowsExceptionWhenNotFound() {
        when(tennisCourtRepository.findById(1)).thenReturn(empty());

        TennisCourtDTO newTennisCourtDTO = new TennisCourtDTO("UpdatedCourt", "UpdatedDetails", location.getId());

        assertThrows(TennisCourtDoesNotExistsException.class, () -> tennisCourtService.updateTennisCourt(1, newTennisCourtDTO));
    }

    @Test
    void deleteTennisCourt() throws TennisCourtDoesNotExistsException {
        when(tennisCourtRepository.existsById(1)).thenReturn(true);

        tennisCourtService.deleteTennisCourt(1);

        verify(tennisCourtRepository).deleteById(1);
    }

    @Test
    void deleteTennisCourtThrowsExceptionWhenNotFound() {
        when(tennisCourtRepository.existsById(1)).thenReturn(false);

        assertThrows(TennisCourtDoesNotExistsException.class, () -> tennisCourtService.deleteTennisCourt(1));
    }
}