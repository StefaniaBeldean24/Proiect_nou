package com.internship.backend.service;

import com.internship.backend.dto.PriceDTO;
import com.internship.backend.exceptions.PriceIdDoesNotExistException;
import com.internship.backend.exceptions.TennisCourtDoesNotExistsException;
import com.internship.backend.model.Price;
import com.internship.backend.model.TennisCourt;
import com.internship.backend.repository.PriceRepository;
import com.internship.backend.repository.TennisCourtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PriceService {

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private TennisCourtRepository tennisCourtRepository;

    public List<Price> getAllPrices() {
        return priceRepository.findAll();
    }

    public Price addPrice(final PriceDTO priceDTO) throws TennisCourtDoesNotExistsException {
        var newPrice = buildPrice(priceDTO);
        return addPrice(newPrice);
    }

    public Price addPrice(final Price price) {
        return priceRepository.save(price);
    }

    private Price buildPrice(final PriceDTO priceDTO) throws TennisCourtDoesNotExistsException {
        var tennisCourt = findTennisCourt(priceDTO);
        return createPrice(priceDTO, tennisCourt);
    }

    private TennisCourt findTennisCourt(final PriceDTO priceDTO) throws TennisCourtDoesNotExistsException {
        return tennisCourtRepository.findByName(priceDTO.getTennisCourtName())
                .orElseThrow(() -> new TennisCourtDoesNotExistsException("Tennis Court not found"));
    }

    private Price createPrice(final PriceDTO priceDTO, final TennisCourt tennisCourt) {
        var price = new Price();
        price.setId(UUID.randomUUID().toString());
        price.setSeason(priceDTO.getSeason());
        price.setPeriodOfDay(priceDTO.getPeriodOfDay());
        price.setPrice(priceDTO.getPrice());
        price.setTennisCourt(tennisCourt);
        return price;
    }

    public Price update(final String tennisCourtName, final PriceDTO updatedPriceDTO) throws PriceIdDoesNotExistException, TennisCourtDoesNotExistsException {
        var tennisCourt = tennisCourtRepository.findByName(tennisCourtName)
                .orElseThrow(() -> new TennisCourtDoesNotExistsException("Tennis Court not found"));

        var price = priceRepository.findPriceByTennisCourtName(tennisCourtName).stream()
                .filter(p -> p.getTennisCourt().getName().equals(tennisCourtName))
                .findFirst()
                .orElseThrow(() -> new PriceIdDoesNotExistException("Price ID not found"));

        updateOldPrice(price, updatedPriceDTO, tennisCourt);

        return priceRepository.save(price);
    }

    public void updateOldPrice(final Price price, final PriceDTO updatedPriceDTO, final TennisCourt tennisCourt) {
        price.setSeason(updatedPriceDTO.getSeason());
        price.setPeriodOfDay(updatedPriceDTO.getPeriodOfDay());
        price.setPrice(updatedPriceDTO.getPrice());
        price.setTennisCourt(tennisCourt);
    }

    public void delete(final String tennisCourtName) throws TennisCourtDoesNotExistsException {
        var tennisCourt = tennisCourtRepository.findByName(tennisCourtName)
                .orElseThrow(() -> new TennisCourtDoesNotExistsException("Tennis Court not found"));

        var priceToDelete = priceRepository.findPriceByTennisCourtName(tennisCourtName).stream()
                .filter(price -> price.getTennisCourt().getName().equals(tennisCourtName))
                .findFirst()
                .orElseThrow(() -> new TennisCourtDoesNotExistsException("Price not found"));

        tennisCourt.getPrices().remove(priceToDelete);
        priceRepository.delete(priceToDelete);
    }
}