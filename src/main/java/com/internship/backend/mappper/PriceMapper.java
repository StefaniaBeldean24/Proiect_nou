package com.internship.backend.mappper;

import com.internship.backend.dto.PriceDTO;
import com.internship.backend.exceptions.TennisCourtDoesNotExistsException;
import com.internship.backend.model.Price;
import com.internship.backend.model.TennisCourt;
import com.internship.backend.repository.TennisCourtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PriceMapper {

    @Autowired
    TennisCourtRepository tennisCourtRepository;

    public Price priceMapper(PriceDTO priceDTO) throws TennisCourtDoesNotExistsException {
        Price price = new Price();
        price.setSeason(priceDTO.getSeason());
        price.setPeriodOfDay(priceDTO.getPeriodOfDay());
        price.setPrice(priceDTO.getPrice());

        TennisCourt tennisCourt = tennisCourtRepository.findById(priceDTO.getTennisCourtId()).orElseThrow(()-> new TennisCourtDoesNotExistsException("TennisCourt not found"));
        price.setTennisCourt(tennisCourt);

        return price;
    }
}
