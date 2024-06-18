package com.internship.backend.controller;

import com.internship.backend.dto.TennisCourtDTO;
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

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/tennisCourts")
public class TennisCourtController {

    Logger Log = LoggerFactory.getLogger(TennisCourtController.class);

    @Autowired
    private TennisCourtService tennisCourtService;

    @PostMapping("/add")
    public ResponseEntity<TennisCourt> addTennisCourt(@RequestBody TennisCourtDTO tennisCourtDTO){
        try{
            TennisCourt tennisCourt = tennisCourtService.fromDTO(tennisCourtDTO);
            tennisCourtService.addTennisCourt(tennisCourt);
            Log.info("Added tennis court "+ tennisCourt.getName());
            return ok(tennisCourt);
        }catch (TennisCourtAlreadyExistsException e){
            Log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

    }

    @GetMapping("/getAll")
    public ResponseEntity<List<TennisCourt>> getAllTennisCourts() {
        Optional<List<TennisCourt>> tennisCourts = Optional.ofNullable(tennisCourtService.getAllTennisCourts());
        if(tennisCourts.isPresent()) {
            Log.info("Get all: ", tennisCourts);
            return ok(tennisCourtService.getAllTennisCourts());
        }
        else {
            Log.error("No tennis court found");
            return notFound().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<TennisCourt> updateTennisCourt(@PathVariable("id") int tennisCourtId, @RequestBody TennisCourtDTO tennisCourtDTO){
        try{
            TennisCourt tennisCourt = tennisCourtService.fromDTO(tennisCourtDTO);
            Optional<TennisCourt> updatedTennisCourt = Optional.ofNullable(tennisCourtService.updateTennisCourt(tennisCourtId, tennisCourt));
            Log.info("Updating tennisCourt: ", tennisCourt);
            return ok(updatedTennisCourt.get());
        }catch(TennisCourtDoesNotExistsException e)
        {
            Log.error("Error processing update " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<TennisCourt> deleteTennisCourt(@PathVariable("id") int tennisCourtId){
        try{
            Log.info("Deleting tennisCourt: " + tennisCourtId);
            tennisCourtService.deleteTennisCourt(tennisCourtId);
            return ResponseEntity.ok().build();
        }catch(TennisCourtDoesNotExistsException e){
            Log.error("Error processing delete "+ e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
