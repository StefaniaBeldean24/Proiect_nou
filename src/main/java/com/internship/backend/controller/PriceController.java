package com.internship.backend.controller;

import com.internship.backend.dto.PriceDTO;
import com.internship.backend.model.Price;
import com.internship.backend.service.PriceService;
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
@RequestMapping("api/prices")
public class PriceController {

    Logger Log = LoggerFactory.getLogger(PriceController.class);

    @Autowired
    private PriceService priceService;

    @PostMapping("/add")
    public Price addPrice(@RequestBody PriceDTO priceDTO){
        Price price = priceService.fromDTO(priceDTO);
        priceService.addPrice(price);
        return new ResponseEntity<>(price, HttpStatus.CREATED).getBody();
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Price>> getAllPrices(){
        Optional<List<Price>> price = Optional.ofNullable(priceService.getAllPrices());
        if(price.isPresent()){
            return ok(priceService.getAllPrices());
        }
        else{
            return notFound().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Price> updatePrice(@PathVariable("id") int priceId, @RequestBody PriceDTO priceDTO){
        try{
            Price price = priceService.fromDTO(priceDTO);
            Optional<Price> updatedPrice = Optional.ofNullable(priceService.update(priceId, price));
            Log.info("Get all"+ price.toString());
            return ok(updatedPrice.get());
        }
        catch(EntityNotFoundException e){
            Log.error("Error processing update "+e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Price> deletePrice(@PathVariable("id") int priceId){
        try{
            Log.info("Deleting price: "+ priceId);
            priceService.delete(priceId);
            return ResponseEntity.ok().build();
        }catch (RuntimeException e) {
            Log.error("Error processing delete " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
