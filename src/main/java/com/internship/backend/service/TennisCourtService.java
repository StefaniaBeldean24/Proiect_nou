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

    public TennisCourt addTennisCourt(final TennisCourtDTO tennisCourtDTO) throws TennisCourtAlreadyExistsException, LocationDoesNotExistException {
        TennisCourt newTennisCourt = buildTennisCourt(tennisCourtDTO);
        return addTennisCourt(newTennisCourt);
    }

    private TennisCourt buildTennisCourt(final TennisCourtDTO tennisCourtDTO) throws LocationDoesNotExistException, TennisCourtAlreadyExistsException {
        checkTennisCourtExists(tennisCourtDTO);

        var location = findLocation(tennisCourtDTO);

        var tennisCourt = createTennisCourt(tennisCourtDTO, location);

        return tennisCourt;
    }

    private void checkTennisCourtExists(final TennisCourtDTO tennisCourtDTO) throws TennisCourtAlreadyExistsException {
        if (tennisCourtRepository.findByName(tennisCourtDTO.getName()).isPresent()) {
            throw new TennisCourtAlreadyExistsException("Tennis Court already exists");
        }
    }

    private Location findLocation(final TennisCourtDTO tennisCourtDTO) throws LocationDoesNotExistException {
        return locationRepository.findByName(tennisCourtDTO.getLocationName())
                .orElseThrow(() -> new LocationDoesNotExistException("Location not found"));
    }

    private TennisCourt createTennisCourt(final TennisCourtDTO tennisCourtDTO, final Location location) {
        TennisCourt tennisCourt = new TennisCourt();
        tennisCourt.setId(UUID.randomUUID().toString());
        tennisCourt.setName(tennisCourtDTO.getName());
        tennisCourt.setDetails(tennisCourtDTO.getDetails());
        tennisCourt.setLocation(location);
        return tennisCourt;
    }

    public TennisCourt addTennisCourt(final TennisCourt tennisCourt) {
        return tennisCourtRepository.save(tennisCourt);
    }

    public TennisCourt updateTennisCourt(final String tennisCourtName, final TennisCourtDTO newTennisCourtDTO) throws TennisCourtDoesNotExistsException, LocationDoesNotExistException {
        var tennisCourt = tennisCourtRepository.findByName(tennisCourtName)
                .orElseThrow(() -> new TennisCourtDoesNotExistsException("Tennis Court not found"));

        updateOldTennisCourt(tennisCourt, newTennisCourtDTO);
        return tennisCourtRepository.save(tennisCourt);
    }

    public void updateOldTennisCourt(final TennisCourt tennisCourt, final TennisCourtDTO newTennisCourtDTO) throws LocationDoesNotExistException {
        var location = locationRepository.findAll().stream()
                .filter(loc -> loc.getName().equals(newTennisCourtDTO.getLocationName()))
                .findFirst()
                .orElseThrow(() -> new LocationDoesNotExistException("Location not found"));

        tennisCourt.setName(newTennisCourtDTO.getName());
        tennisCourt.setDetails(newTennisCourtDTO.getDetails());
        tennisCourt.setLocation(location);
    }

    public void deleteTennisCourt(final String tennisCourtName) throws TennisCourtDoesNotExistsException {
        var tennisCourtToDelete = tennisCourtRepository.findByName(tennisCourtName).stream()
                .filter(tennisCourt -> tennisCourt.getName().equals(tennisCourtName))
                .findFirst()
                .orElseThrow(() -> new TennisCourtDoesNotExistsException("Tennis Court not found"));

        tennisCourtRepository.delete(tennisCourtToDelete);
    }
}
