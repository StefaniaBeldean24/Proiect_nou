package com.internship.backend.service;

import com.internship.backend.dto.LocationDTO;
import com.internship.backend.exceptions.LocationAlreadyExistsException;
import com.internship.backend.exceptions.LocationDoesNotExistException;
import com.internship.backend.mappper.LocationMapper;
import com.internship.backend.model.Location;
import com.internship.backend.repository.LocationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    private LocationMapper locationMapper = new LocationMapper();

    public List<Location> getAllLocations(){
        return locationRepository.findAll();
    }

    public Location addLocation(LocationDTO locationDTO) throws LocationAlreadyExistsException {
        Location location = locationMapper.mapToLocation(locationDTO);

        if (locationRepository.existsByName(location.getName())) {
            throw new LocationAlreadyExistsException("Location already exists");
        }
        return locationRepository.save(location);
    }

    public Location update(int locationId, LocationDTO updatedLocationDTO) throws LocationDoesNotExistException {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new LocationDoesNotExistException("Location not found"));

        location.setName(updatedLocationDTO.getName());
        location.setDetails(updatedLocationDTO.getDetails());

        return locationRepository.save(location);
    }

    public void delete(int locationID) throws LocationDoesNotExistException {
        if (!locationRepository.existsById(locationID)) {
            throw new LocationDoesNotExistException("Location id does not exist");
        }
        locationRepository.deleteById(locationID);
        if (locationRepository.count() == 0) {
            locationRepository.resetAutoIncrementId();
        }

    }
}