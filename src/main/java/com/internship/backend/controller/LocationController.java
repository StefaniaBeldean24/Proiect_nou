package com.internship.backend.controller;

import com.internship.backend.dto.LocationDTO;
import com.internship.backend.model.Location;
import com.internship.backend.service.LocationService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/locations")
public class LocationController {

    Logger Log = LoggerFactory.getLogger(LocationController.class);

    @Autowired
    private LocationService locationService;

    @PostMapping("/add")
    public Location addLocation(@RequestBody LocationDTO locationDTO) {
        //if user is admin
        Location baseLocation = locationService.fromDTO(locationDTO);
        baseLocation = locationService.addLocation(baseLocation);
        //else throw error
        return new ResponseEntity<>(baseLocation, HttpStatus.CREATED).getBody();
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Location>> getAllLocations() {
        Optional<List<Location>> location = Optional.ofNullable(locationService.getAllLocations());
        if(location.isPresent()) {
            return ok(locationService.getAllLocations());
        }
        else {
            return notFound().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Location> updateLocation(@PathVariable("id") int locationId, @RequestBody LocationDTO locationDTO){
        try{
            Location location = locationService.fromDTO(locationDTO);
            Optional<Location> updatedLocation = Optional.ofNullable(locationService.update(locationId, location));
            Log.info("Updating location: " ,location.toString());
            return ok(updatedLocation.get());
        }catch(EntityNotFoundException e){
            Log.error("Error processing update "+ e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Location> deleteLocation(@PathVariable("id") int locationID){
        try{
            Log.info("Deleting location: ", locationID);
            locationService.delete(locationID);
            return ResponseEntity.ok().build();
        }catch (RuntimeException e){
            Log.error("Error processing delete ", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

}
