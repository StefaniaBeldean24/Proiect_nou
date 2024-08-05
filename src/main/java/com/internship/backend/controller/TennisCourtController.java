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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
            tennisCourtService.addTennisCourt(tennisCourtDTO);
            Log.info("Added tennis court ");
            return ok().build();
        } catch (TennisCourtAlreadyExistsException | LocationDoesNotExistException e) {
            Log.error(e.getMessage());
            return status(HttpStatus.CONFLICT).body(null);
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<TennisCourt>> getAllTennisCourts() {
        Optional<List<TennisCourt>> tennisCourts = Optional.ofNullable(tennisCourtService.getAllTennisCourts());
        if (tennisCourts.isPresent()) {
            Log.info("Get all: ", tennisCourts);
            return ok(tennisCourtService.getAllTennisCourts());
        } else {
            Log.error("No tennis court found");
            return notFound().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<TennisCourt> updateTennisCourt(@PathVariable("Tennis Court name") String tennisCourtName, @RequestBody TennisCourtDTO tennisCourtDTO) {
        try {
            tennisCourtService.updateTennisCourt(tennisCourtName, tennisCourtDTO);
            Log.info("Updating tennisCourt: ");
            return ok().build();
        } catch (TennisCourtDoesNotExistsException | LocationDoesNotExistException e) {
            Log.error("Error processing update " + e.getMessage());
            return notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<TennisCourt> deleteTennisCourt(@PathVariable("Tennis Court name") String tennisCourtName) {
        try {
            Log.info("Deleting tennisCourt: ");
            tennisCourtService.deleteTennisCourt(tennisCourtName);
            return ok().build();
        } catch (TennisCourtDoesNotExistsException e) {
            Log.error("Error processing delete " + e.getMessage());
            return notFound().build();
        }
    }
}
