package com.internship.backend.controller;

import com.internship.backend.dto.TennisCourtDTO;
import com.internship.backend.exceptions.LocationDoesNotExistException;
import com.internship.backend.exceptions.TennisCourtAlreadyExistsException;
import com.internship.backend.exceptions.TennisCourtDoesNotExistsException;
import com.internship.backend.model.TennisCourt;
import com.internship.backend.service.TennisCourtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("api/tennisCourts")
public class TennisCourtController {

    Logger Log = LoggerFactory.getLogger(TennisCourtController.class);

    @Autowired
    private TennisCourtService tennisCourtService;

    @PostMapping("/add")
    public ResponseEntity<TennisCourt> addTennisCourt(@RequestBody TennisCourtDTO tennisCourtDTO) {
        try {
            Log.info("Added tennis court");
            return ok(tennisCourtService.addTennisCourt(tennisCourtDTO));
        } catch (TennisCourtAlreadyExistsException e) {
            Log.error(e.getMessage());
            return status(CONFLICT).body(null);
        } catch (LocationDoesNotExistException e) {
            return status(CONFLICT).body(null);
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<TennisCourt>> getAllTennisCourts() {
        List<TennisCourt> tennisCourts = tennisCourtService.getAllTennisCourts();
        if (ofNullable(tennisCourts).isPresent()) {
            Log.info("Get all tennis courts: ");
            return ok(tennisCourts);
        } else {
            Log.error("No tennis court found");
            return notFound().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<TennisCourt> updateTennisCourt(@PathVariable("id") int tennisCourtId, @RequestBody TennisCourtDTO tennisCourtDTO) {
        try {
            var updatedTennisCourt = ofNullable(tennisCourtService.updateTennisCourt(tennisCourtId, tennisCourtDTO));
            Log.info("Updating tennisCourt");
            return ok(updatedTennisCourt.get());
        } catch (TennisCourtDoesNotExistsException e) {
            Log.error("Error processing update " + e.getMessage());
            return notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<TennisCourt> deleteTennisCourt(@PathVariable("id") int tennisCourtId) {
        try {
            Log.info("Deleting tennisCourt: " + tennisCourtId);
            tennisCourtService.deleteTennisCourt(tennisCourtId);
            return ok().build();
        } catch (TennisCourtDoesNotExistsException e) {
            Log.error("Error processing delete " + e.getMessage());
            return notFound().build();
        }
    }
}
