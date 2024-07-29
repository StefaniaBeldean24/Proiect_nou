package com.internship.backend.service;

import com.internship.backend.dto.PriceDTO;
import com.internship.backend.exceptions.PriceIdDoesNotExistException;
import com.internship.backend.exceptions.TennisCourtDoesNotExistsException;
import com.internship.backend.mappper.PriceMapper;
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

    @Autowired
    private PriceMapper priceMapper;

    public List<Price> getAllPrices(){
        return priceRepository.findAll();
    }

    public Price addPrice(PriceDTO priceDTO) throws TennisCourtDoesNotExistsException {
        Price price = priceMapper.priceMapper(priceDTO);
        return priceRepository.save(price);
    }

    public Price update(int priceId, PriceDTO updatedPriceDTO) throws PriceIdDoesNotExistException {
        Price price = priceRepository.findById(priceId).orElseThrow(()-> new PriceIdDoesNotExistException("Price not found"));

        price.setPrice(updatedPriceDTO.getPrice());
        price.setSeason(updatedPriceDTO.getSeason());
        price.setPeriodOfDay(updatedPriceDTO.getPeriodOfDay());

        if (priceRepository.count() == 0) {
            priceRepository.resetAutoIncrementId();
        }

        return priceRepository.save(price);

    }

    public void delete(int priceId){
        if(!priceRepository.existsById(priceId))
            throw new RuntimeException("Price does not exist");

        priceRepository.deleteById(priceId);

        if(priceRepository.count() == 0){
            priceRepository.resetAutoIncrementId();
        }
    }
}
