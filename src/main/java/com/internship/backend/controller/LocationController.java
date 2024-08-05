package com.internship.backend.controller;

import com.internship.backend.dto.LocationDTO;
import com.internship.backend.exceptions.LocationAlreadyExistsException;
import com.internship.backend.exceptions.LocationDoesNotExistException;
import com.internship.backend.model.Location;
import com.internship.backend.service.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("api/locations")
public class LocationController {

    Logger Log = LoggerFactory.getLogger(LocationController.class);

    @Autowired
    private LocationService locationService;

    @PostMapping("/add")
    public ResponseEntity<Location> addLocation(@RequestBody LocationDTO locationDTO) {
        try {
            locationService.addLocation(locationDTO);
            Log.info("Added location " + locationDTO);
            return ok().build();
        } catch (LocationAlreadyExistsException e) {
            Log.error(e.getMessage());
            return status(CONFLICT).body(null);
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Location>> getAllLocations() {
        Optional<List<Location>> location = Optional.ofNullable(locationService.getAllLocations());
        if (location.isPresent()) {
            Log.info("Get all: ", location);
            return ok(locationService.getAllLocations());
        } else {
            Log.error("No location found");
            return notFound().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Location> updateLocation(@PathVariable("enter the name of location") String oldLocationName, @RequestBody LocationDTO locationDTO) {
        try {
            locationService.update(oldLocationName, locationDTO);
            return ok().build();
        } catch (LocationDoesNotExistException e) {
            Log.error("Error processing update " + e.getMessage());
            return notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Location> deleteLocation(@PathVariable("enter the name of location") String locationName) {
        try {
            Log.info("Deleting location: ");
            locationService.delete(locationName);
            return ok().build();
        } catch (LocationDoesNotExistException e) {
            Log.error("Error processing delete ", e.getMessage());
            return notFound().build();
        }
    }
}