package com.internship.backend.service;

import com.internship.backend.dto.TennisCourtDTO;
import com.internship.backend.exceptions.TennisCourtAlreadyExistsException;
import com.internship.backend.exceptions.TennisCourtDoesNotExistsException;
import com.internship.backend.mappper.TennisCourtMapper;
import com.internship.backend.model.Location;
import com.internship.backend.model.TennisCourt;
import com.internship.backend.repository.LocationRepository;
import com.internship.backend.repository.TennisCourtRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        location = Location.builder()
                .id(1)
                .name("Location1")
                .tennisCourt(new ArrayList<>())
                .build();

        tennisCourt = TennisCourt.builder()
                .id(1)
                .name("Court1")
                .details("Details")
                .location(location)
                .build();

        tennisCourtDTO = new TennisCourtDTO("Court1", "Details", location.getId());

        tennisCourts = new ArrayList<>();
        tennisCourts.add(tennisCourt);
    }

    @Test
    void getAllTennisCourts() {
        when(tennisCourtRepository.findAll()).thenReturn(tennisCourts);

        List<TennisCourt> result = tennisCourtService.getAllTennisCourts();

        assertEquals(1, result.size());
        assertEquals(tennisCourt, result.get(0));
    }

    @Test
    void addTennisCourt() throws TennisCourtAlreadyExistsException {
        when(tennisCourtMapper.tennisCourtMapper(any(TennisCourtDTO.class))).thenReturn(tennisCourt);
        when(locationRepository.findAll()).thenReturn(List.of(location));
        when(tennisCourtRepository.save(any(TennisCourt.class))).thenReturn(tennisCourt);

        TennisCourt savedTennisCourt = tennisCourtService.addTennisCourt(tennisCourtDTO);

        assertEquals("Court1", savedTennisCourt.getName());
        verify(tennisCourtRepository).save(any(TennisCourt.class));
    }

    @Test
    void addTennisCourtThrowsExceptionWhenAlreadyExists() {
        location.getTennisCourt().add(tennisCourt);
        when(tennisCourtMapper.tennisCourtMapper(any(TennisCourtDTO.class))).thenReturn(tennisCourt);
        when(locationRepository.findAll()).thenReturn(List.of(location));

        assertThrows(TennisCourtAlreadyExistsException.class, () -> tennisCourtService.addTennisCourt(tennisCourtDTO));
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
        when(tennisCourtRepository.findById(1)).thenReturn(Optional.empty());

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