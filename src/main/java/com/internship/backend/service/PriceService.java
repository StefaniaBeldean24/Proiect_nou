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

    private PriceMapper priceMapper = new PriceMapper();

    public List<Price> getAllPrices(){
        return priceRepository.findAll();
    }

    public Price addPrice(PriceDTO priceDTO) throws TennisCourtDoesNotExistsException {
        TennisCourt tennisCourt = tennisCourtRepository.findById(priceDTO.getTennisCourtId())
                .orElseThrow(()-> new TennisCourtDoesNotExistsException("TennisCourt not found"));

        Price price = priceMapper.mapToPrice(priceDTO, tennisCourt);
        return priceRepository.save(price);
    }

    public Price update(int priceId, PriceDTO updatedPriceDTO) throws PriceIdDoesNotExistException, TennisCourtDoesNotExistsException {
        Price price = priceRepository.findById(priceId)
                .orElseThrow(()-> new PriceIdDoesNotExistException("Price not found"));

        TennisCourt tennisCourt = tennisCourtRepository.findById(updatedPriceDTO.getTennisCourtId())
                .orElseThrow(()-> new TennisCourtDoesNotExistsException("TennisCourt not found"));

        price.setPrice(updatedPriceDTO.getPrice());
        price.setSeason(updatedPriceDTO.getSeason());
        price.setPeriodOfDay(updatedPriceDTO.getPeriodOfDay());
        price.setTennisCourt(tennisCourt);

        if (priceRepository.count() == 0) {
            priceRepository.resetAutoIncrementId();
        }

        return priceRepository.save(price);
    }

    public void delete(int priceId) throws PriceIdDoesNotExistException {
        if(!priceRepository.existsById(priceId))
            throw new PriceIdDoesNotExistException("Price does not exist");
        priceRepository.deleteById(priceId);
        if(priceRepository.count() == 0){
            priceRepository.resetAutoIncrementId();
        }
    }
}
