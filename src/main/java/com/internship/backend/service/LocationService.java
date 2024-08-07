package com.internship.backend.service;

import com.internship.backend.dto.LocationDTO;
import com.internship.backend.exceptions.LocationAlreadyExistsException;
import com.internship.backend.exceptions.LocationDoesNotExistException;
import com.internship.backend.model.Location;
import com.internship.backend.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public Location addLocation(final LocationDTO locationDTO) throws LocationAlreadyExistsException {
        var newLocation = buildLocation(locationDTO);
        return addLocation(newLocation);
    }

    public Location addLocation(final Location location) {
        return locationRepository.save(location);
    }

    private Location buildLocation(final LocationDTO locationDTO) throws LocationAlreadyExistsException {
        checkLocationExists(locationDTO);
        return createLocation(locationDTO);
    }

    private void checkLocationExists(final LocationDTO locationDTO) throws LocationAlreadyExistsException {
        if (locationRepository.findByName(locationDTO.getName()).isPresent()) {
            throw new LocationAlreadyExistsException("Location already exists");
        }
    }

    private Location createLocation(final LocationDTO locationDTO) {
        var location = new Location();
        location.setId(UUID.randomUUID().toString());
        location.setName(locationDTO.getName());
        location.setDetails(locationDTO.getDetails());
        return location;
    }

    public Location update(final String oldLocationName, final LocationDTO updatedLocationDTO) throws LocationDoesNotExistException {
        var location = locationRepository.findByName(oldLocationName)
                .orElseThrow(() -> new LocationDoesNotExistException("Location not found"));

        updateOldLocation(location, updatedLocationDTO);
        return locationRepository.save(location);
    }

    public void updateOldLocation(final Location location, final LocationDTO newLocation) {
        location.setName(newLocation.getName());
        location.setDetails(newLocation.getDetails());
    }

    public void delete(final String locationName) throws LocationDoesNotExistException {
        var locationToDelete = locationRepository.findByName(locationName)
                .orElseThrow(() -> new LocationDoesNotExistException("Location not found"));

        locationRepository.delete(locationToDelete);
    }
}