package com.internship.backend.controller;

import com.internship.backend.dto.PriceDTO;
import com.internship.backend.exceptions.PriceIdDoesNotExistException;
import com.internship.backend.exceptions.TennisCourtDoesNotExistsException;
import com.internship.backend.model.Price;
import com.internship.backend.service.PriceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("api/prices")
public class PriceController {

    Logger Log = LoggerFactory.getLogger(PriceController.class);

    @Autowired
    private PriceService priceService;

    @PostMapping("/add")
    public ResponseEntity<Price> addPrice(@RequestBody PriceDTO priceDTO) {
        try {
            priceService.addPrice(priceDTO);
            return ok().build();
        } catch (TennisCourtDoesNotExistsException e) {
            return badRequest().build();
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Price>> getAllPrices() {
        Optional<List<Price>> price = Optional.ofNullable(priceService.getAllPrices());
        if (price.isPresent()) {
            return ok(priceService.getAllPrices());
        } else {
            return notFound().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Price> updatePrice(@PathVariable("Tennis Court name") String tennisCourtName, @RequestBody PriceDTO priceDTO) {
        try {
            priceService.update(tennisCourtName, priceDTO);
            Log.info("Update price");
            return ok().build();
        } catch (TennisCourtDoesNotExistsException | PriceIdDoesNotExistException e) {
            return badRequest().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Price> deletePrice(@PathVariable("Tennis Court name") String tennisCourtName) {
        try {
            Log.info("Deleting price: ");
            priceService.delete(tennisCourtName);
            return ok().build();
        } catch (RuntimeException | TennisCourtDoesNotExistsException e) {
            Log.error("Error processing delete " + e.getMessage());
            return notFound().build();
        }
    }
}
