package com.internship.backend.service;

import com.internship.backend.dto.LocationDTO;
import com.internship.backend.exceptions.LocationAlreadyExistsException;
import com.internship.backend.exceptions.LocationDoesNotExistException;
import com.internship.backend.model.Location;
import com.internship.backend.model.TennisCourt;
import com.internship.backend.repository.LocationRepository;
import com.internship.backend.repository.TennisCourtRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocationService {
    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private TennisCourtRepository tennisCourtRepository;

    public List<Location> getAllLocations(){
        return locationRepository.findAll();
    }

    public Location addLocation(Location location) throws LocationAlreadyExistsException {

        if(locationRepository.findByName(location.getName()) != null){
            throw new LocationAlreadyExistsException("Location already exists");
        }
        return locationRepository.save(location);
    }

    public Location fromDTO(LocationDTO locationDTO){
        Location location = new Location();
        location.setId(locationDTO.getId());
        location.setName(locationDTO.getName());
        location.setDetails(locationDTO.getDetails());

        List<TennisCourt> tennisCourts = new ArrayList<>();
        for (TennisCourt elem : tennisCourtRepository.findAll()){
            if (elem.getId() == locationDTO.getId()) {
                tennisCourts.add(elem);
            }
        }
        location.setTennisCourt(tennisCourts);
        return location;
    }

    public Location update(int locationId, Location updatedLocation) throws LocationDoesNotExistException {
         Location location = locationRepository.findById(locationId).orElseThrow(()->new LocationDoesNotExistException("Location not found"));

         location.setName(updatedLocation.getName());
         location.setDetails(updatedLocation.getDetails());

         return locationRepository.save(location);
    }

    public void delete(int locationID) throws LocationDoesNotExistException {
        if(!locationRepository.existsById(locationID))
            throw new LocationDoesNotExistException("Location does not exist");

        if (locationRepository.count() == 0) {
            locationRepository.resetAutoIncrementId();
        }

        locationRepository.deleteById(locationID);
    }
}
