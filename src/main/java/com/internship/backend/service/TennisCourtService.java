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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TennisCourtService {
    @Autowired
    private TennisCourtRepository tennisCourtRepository;

    @Autowired
    private LocationRepository locationRepository;

    TennisCourtMapper tennisCourtMapper = new TennisCourtMapper();

    public List<TennisCourt> getAllTennisCourts(){
        return tennisCourtRepository.findAll();
    }

    public TennisCourt addTennisCourt(TennisCourtDTO tennisCourtDTO) throws TennisCourtAlreadyExistsException, LocationDoesNotExistException {
        Location location = locationRepository.findById(tennisCourtDTO.getLocationId())
                .orElseThrow(() -> new LocationDoesNotExistException("Location not found"));

        TennisCourt tennisCourt = tennisCourtMapper.mapToTennisCourt(tennisCourtDTO, location);
        Optional<Location> locationOptional = findLocation(tennisCourt.getLocation().getId());

        if (locationOptional.isPresent()) {
            if (!location.getTennisCourt().contains(tennisCourt)) {
                List<TennisCourt> tennisCourtList = location.getTennisCourt();
                tennisCourtList.add(tennisCourt);
                location.setTennisCourt(tennisCourtList);
            } else {
                throw new TennisCourtAlreadyExistsException("TennisCourt already exists");
            }
        }
        return tennisCourtRepository.save(tennisCourt);
    }

    private Optional<Location> findLocation(int locationId) {
        return locationRepository.findAll()
                .stream()
                .filter(l -> l.getId() == locationId)
                .findFirst();
    }

    public TennisCourt updateTennisCourt(int tennisCourtId, TennisCourtDTO newTennisCourtDTO) throws TennisCourtDoesNotExistsException {
        TennisCourt tennisCourt = tennisCourtRepository.findById(tennisCourtId)
                .orElseThrow(() -> new TennisCourtDoesNotExistsException("TennisCourt not found!"));

        tennisCourt.setName(newTennisCourtDTO.getName());
        tennisCourt.setDetails(newTennisCourtDTO.getDetails());

        return tennisCourtRepository.save(tennisCourt);
    }

    public void resetAutoIncrement() {
        if (locationRepository.count() == 0) {
            locationRepository.resetAutoIncrementId();
        }
    }

    public void deleteTennisCourt(int tennisCourtId) throws TennisCourtDoesNotExistsException {
        if (!tennisCourtRepository.existsById(tennisCourtId))
            throw new TennisCourtDoesNotExistsException("TennisCourt does not exist");

        tennisCourtRepository.deleteById(tennisCourtId);

        resetAutoIncrement();
    }
}