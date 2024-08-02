package com.internship.backend.service;

import com.internship.backend.dto.PriceDTO;
import com.internship.backend.exceptions.PriceIdDoesNotExistException;
import com.internship.backend.exceptions.TennisCourtDoesNotExistsException;
import com.internship.backend.mappper.PriceMapper;
import com.internship.backend.model.Price;
import com.internship.backend.model.TennisCourt;
import com.internship.backend.repository.PriceRepository;
import com.internship.backend.repository.TennisCourtRepository;
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

    public List<Price> getAllPrices() {
        return priceRepository.findAll();
    }

    public Price addPrice(PriceDTO priceDTO) throws TennisCourtDoesNotExistsException {
        TennisCourt tennisCourt = tennisCourtRepository.findById(priceDTO.getTennisCourtId())
                .orElseThrow(() -> new TennisCourtDoesNotExistsException("TennisCourt not found"));

        Price price = priceMapper.mapToPrice(priceDTO, tennisCourt);
        return priceRepository.save(price);
    }

    public Price update(int priceId, PriceDTO updatedPriceDTO) throws PriceIdDoesNotExistException, TennisCourtDoesNotExistsException {
        Price price = priceRepository.findById(priceId)
                .orElseThrow(() -> new PriceIdDoesNotExistException("Price not found"));

        TennisCourt tennisCourt = tennisCourtRepository.findById(updatedPriceDTO.getTennisCourtId())
                .orElseThrow(() -> new TennisCourtDoesNotExistsException("TennisCourt not found"));

        setPriceSeasonPeriodTennisCourt(price, updatedPriceDTO.getPrice(), updatedPriceDTO.getSeason(),
                updatedPriceDTO.getPeriodOfDay(), tennisCourt);

        resetAutoIncrement();

        return priceRepository.save(price);
    }

    public void resetAutoIncrement() {
        if (priceRepository.count() == 0) {
            priceRepository.resetAutoIncrementId();
        }
    }

    public void setPriceSeasonPeriodTennisCourt(Price price, Integer cost, String season, String periodOfDay, TennisCourt tennisCourt) {
        price.setPrice(cost);
        price.setSeason(season);
        price.setPeriodOfDay(periodOfDay);
        price.setTennisCourt(tennisCourt);
    }

    public void delete(int priceId) throws PriceIdDoesNotExistException {
        if (!priceRepository.existsById(priceId)) {
            throw new PriceIdDoesNotExistException("Price does not exist");
        }

        priceRepository.deleteById(priceId);
        resetAutoIncrement();
    }
}
