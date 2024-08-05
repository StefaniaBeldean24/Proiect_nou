package com.internship.backend.service;

import com.internship.backend.dto.TennisCourtDTO;
import com.internship.backend.exceptions.LocationDoesNotExistException;
import com.internship.backend.exceptions.TennisCourtAlreadyExistsException;
import com.internship.backend.exceptions.TennisCourtDoesNotExistsException;
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
        TennisCourt newTennisCourt = addIdNameDetailsLocationToTennisCourt(tennisCourtDTO);
        return addTennisCourt(newTennisCourt);
    }

    public TennisCourt addIdNameDetailsLocationToTennisCourt(TennisCourtDTO tennisCourtDTO) throws LocationDoesNotExistException, TennisCourtAlreadyExistsException {
        var tennisCourtExists = tennisCourtRepository.findByName(tennisCourtDTO.getName()).isPresent();
        if (tennisCourtExists) {
            throw new TennisCourtAlreadyExistsException("Tennis Court already exists");
        }

        var location = locationRepository.findByName(tennisCourtDTO.getLocationName())
                .orElseThrow(() -> new LocationDoesNotExistException("Location not found"));

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
        var tennisCourt = tennisCourtRepository.findByName(tennisCourtName)
                .orElseThrow(() -> new TennisCourtDoesNotExistsException("Tennis Court not found"));

        updateOldTennisCourt(tennisCourt, newTennisCourtDTO);
        return tennisCourtRepository.save(tennisCourt);
    }

    public void updateOldTennisCourt(TennisCourt tennisCourt, TennisCourtDTO newTennisCourtDTO) throws LocationDoesNotExistException {
        var location = locationRepository.findAll().stream()
                .filter(loc -> loc.getName().equals(newTennisCourtDTO.getLocationName()))
                .findFirst()
                .orElseThrow(() -> new LocationDoesNotExistException("Location not found"));

        tennisCourt.setName(newTennisCourtDTO.getName());
        tennisCourt.setDetails(newTennisCourtDTO.getDetails());
        tennisCourt.setLocation(location);
    }

    public void deleteTennisCourt(String tennisCourtName) throws TennisCourtDoesNotExistsException {
        var tennisCourtToDelete = tennisCourtRepository.findByName(tennisCourtName).stream()
                .filter(tennisCourt -> tennisCourt.getName().equals(tennisCourtName))
                .findFirst()
                .orElseThrow(() -> new TennisCourtDoesNotExistsException("Tennis Court not found"));

        tennisCourtRepository.delete(tennisCourtToDelete);
    }
}
