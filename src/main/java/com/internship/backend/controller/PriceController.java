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

import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
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
            return ok(priceService.addPrice(priceDTO));
        } catch (TennisCourtDoesNotExistsException e) {
            return status(BAD_REQUEST).build();
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Price>> getAllPrices() {
        List<Price> prices = priceService.getAllPrices();
        if (ofNullable(prices).isPresent()) {
            Log.info("Get all prices: ");
            return ok(prices);
        } else {
            return notFound().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Price> updatePrice(@PathVariable("id") int priceId, @RequestBody PriceDTO priceDTO) {
        try {
            var updatedPrice = ofNullable(priceService.update(priceId, priceDTO));
            Log.info("Updating " + priceDTO);
            return ok(updatedPrice.get());
        } catch (PriceIdDoesNotExistException e) {
            Log.error("Error processing update " + e.getMessage());
            return notFound().build();
        } catch (TennisCourtDoesNotExistsException e) {
            return status(BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Price> deletePrice(@PathVariable("id") int priceId) {
        try {
            Log.info("Deleting price: " + priceId);
            priceService.delete(priceId);
            return ok().build();
        } catch (PriceIdDoesNotExistException e) {
            Log.error("Error processing delete " + e.getMessage());
            return notFound().build();
        }
    }
}
