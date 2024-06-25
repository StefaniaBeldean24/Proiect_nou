package com.internship.backend.service;

import com.internship.backend.dto.PriceDTO;
import com.internship.backend.exceptions.PriceIdDoesNotExistException;
import com.internship.backend.model.Price;
import com.internship.backend.model.TennisCourt;
import com.internship.backend.repository.PriceRepository;
import com.internship.backend.repository.TennisCourtRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceService {

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private TennisCourtRepository tennisCourtRepository;

    public List<Price> getAllPrices(){
        return priceRepository.findAll();
    }

    public Price addPrice(Price price){
        return priceRepository.save(price);
    }

    public Price fromDTO(PriceDTO priceDTO){
        Price price = new Price();
        price.setId(priceDTO.getId());
        price.setSeason(priceDTO.getSeason());
        price.setPeriodOfDay(priceDTO.getPeriodOfDay());
        price.setPrice(priceDTO.getPrice());

        for(TennisCourt court : tennisCourtRepository.findAll()){
            if(court.getId() == priceDTO.getTennisCourtId()){
                price.setTennisCourt(court);
            }
        }
        return price;
    }

    public Price update(int priceId, Price updatedPrice) throws PriceIdDoesNotExistException {
        Price price = priceRepository.findById(priceId).orElseThrow(()-> new PriceIdDoesNotExistException("Price not found"));

        price.setPrice(updatedPrice.getPrice());
        price.setSeason(updatedPrice.getSeason());
        price.setPeriodOfDay(updatedPrice.getPeriodOfDay());

        if (priceRepository.count() == 0) {
            priceRepository.resetAutoIncrementId();
        }

        return priceRepository.save(price);

    }

    public void delete(int priceId){
        if(!priceRepository.existsById(priceId))
            throw new RuntimeException("Price does not exist");

        priceRepository.deleteById(priceId);
    }
}
