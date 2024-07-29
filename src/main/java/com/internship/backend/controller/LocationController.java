package com.internship.backend.controller;

import com.internship.backend.dto.LocationDTO;
import com.internship.backend.exceptions.LocationAlreadyExistsException;
import com.internship.backend.exceptions.LocationDoesNotExistException;
import com.internship.backend.model.Location;
import com.internship.backend.service.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import static java.util.Optional.ofNullable;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("api/locations")
public class LocationController {

    Logger Log = LoggerFactory.getLogger(LocationController.class);

    @Autowired
    private LocationService locationService;

    @PostMapping("/add")
    public ResponseEntity<Location> addLocation(@RequestBody LocationDTO locationDTO){
        try{
            Log.info("Added location " + locationDTO.getName());
            return ok(locationService.addLocation(locationDTO));
        }catch (LocationAlreadyExistsException e) {
            Log.error(e.getMessage());
            return status(HttpStatus.CONFLICT).body(null);
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Location>> getAllLocations() {
        List<Location> locations = locationService.getAllLocations();

        if(ofNullable(locations).isPresent()) {
            Log.info("Get all locations: ");
            return ok(locations);
        } else {
            Log.error("No location found");
            return notFound()
                    .build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Location> updateLocation(@PathVariable("id") int locationId, @RequestBody LocationDTO locationDTO){
        try{
            var updatedLocation = ofNullable(locationService.update(locationId, locationDTO));
            Log.info("Updating location: ");
            return ok(updatedLocation.get());
        } catch(LocationDoesNotExistException e) {
            Log.error("Error processing update "+ e.getMessage());
            return notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Location> deleteLocation(@PathVariable("id") int locationID){
        try{
            Log.info("Deleting location: ", locationID);
            locationService.delete(locationID);
            return ok().build();
        } catch (LocationDoesNotExistException e) {
            Log.error("Error processing delete ", e.getMessage());
            return notFound().build();
        }
    }

}
