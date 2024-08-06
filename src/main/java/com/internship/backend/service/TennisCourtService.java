package com.internship.backend.service;

import com.internship.backend.dto.TennisCourtDTO;
import com.internship.backend.exceptions.LocationDoesNotExistException;
import com.internship.backend.exceptions.TennisCourtAlreadyExistsException;
import com.internship.backend.exceptions.TennisCourtDoesNotExistsException;
import com.internship.backend.model.Location;
import com.internship.backend.model.TennisCourt;
import com.internship.backend.repository.LocationRepository;
import com.internship.backend.repository.TennisCourtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TennisCourtService {
    @Autowired
    private TennisCourtRepository tennisCourtRepository;

    @Autowired
    private LocationRepository locationRepository;

    public List<TennisCourt> getAllTennisCourts() {
        return tennisCourtRepository.findAll();
    }

    public TennisCourt addTennisCourt(TennisCourtDTO tennisCourtDTO) throws TennisCourtAlreadyExistsException, LocationDoesNotExistException {
        TennisCourt newTennisCourt = buildTennisCourt(tennisCourtDTO);
        return addTennisCourt(newTennisCourt);
    }

    private TennisCourt buildTennisCourt(TennisCourtDTO tennisCourtDTO) throws LocationDoesNotExistException, TennisCourtAlreadyExistsException {
        checkTennisCourtExists(tennisCourtDTO);

        var location = findLocation(tennisCourtDTO);

        var tennisCourt = createTennisCourt(tennisCourtDTO, location);

        return tennisCourt;
    }

    private void checkTennisCourtExists(TennisCourtDTO tennisCourtDTO) throws TennisCourtAlreadyExistsException {
        if (tennisCourtRepository.findByName(tennisCourtDTO.getName()).isPresent()) {
            throw new TennisCourtAlreadyExistsException("Tennis Court already exists");
        }
    }

    private Location findLocation(TennisCourtDTO tennisCourtDTO) throws LocationDoesNotExistException {
        return locationRepository.findByName(tennisCourtDTO.getLocationName())
                .orElseThrow(() -> new LocationDoesNotExistException("Location not found"));
    }

    private TennisCourt createTennisCourt(TennisCourtDTO tennisCourtDTO, Location location) {
        TennisCourt tennisCourt = new TennisCourt();
        tennisCourt.setId(UUID.randomUUID().toString());
        tennisCourt.setName(tennisCourtDTO.getName());
        tennisCourt.setDetails(tennisCourtDTO.getDetails());
        tennisCourt.setLocation(location);
        return tennisCourt;
    }

    public TennisCourt addTennisCourt(TennisCourt tennisCourt) {
        return tennisCourtRepository.save(tennisCourt);
    }

    public TennisCourt updateTennisCourt(String tennisCourtName, TennisCourtDTO newTennisCourtDTO) throws TennisCourtDoesNotExistsException, LocationDoesNotExistException {
        final var tennisCourt = tennisCourtRepository.findByName(tennisCourtName)
                .orElseThrow(() -> new TennisCourtDoesNotExistsException("Tennis Court not found"));

        updateOldTennisCourt(tennisCourt, newTennisCourtDTO);
        return tennisCourtRepository.save(tennisCourt);
    }

    public void updateOldTennisCourt(TennisCourt tennisCourt, TennisCourtDTO newTennisCourtDTO) throws LocationDoesNotExistException {
        final var location = locationRepository.findAll().stream()
                .filter(loc -> loc.getName().equals(newTennisCourtDTO.getLocationName()))
                .findFirst()
                .orElseThrow(() -> new LocationDoesNotExistException("Location not found"));

        tennisCourt.setName(newTennisCourtDTO.getName());
        tennisCourt.setDetails(newTennisCourtDTO.getDetails());
        tennisCourt.setLocation(location);
    }

    public void deleteTennisCourt(String tennisCourtName) throws TennisCourtDoesNotExistsException {
        final var tennisCourtToDelete = tennisCourtRepository.findByName(tennisCourtName).stream()
                .filter(tennisCourt -> tennisCourt.getName().equals(tennisCourtName))
                .findFirst()
                .orElseThrow(() -> new TennisCourtDoesNotExistsException("Tennis Court not found"));

        tennisCourtRepository.delete(tennisCourtToDelete);
    }
}
