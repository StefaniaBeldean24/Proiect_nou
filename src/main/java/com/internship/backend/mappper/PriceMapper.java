package com.internship.backend.mappper;

import com.internship.backend.dto.PriceDTO;
import com.internship.backend.model.Price;
import com.internship.backend.model.TennisCourt;

public class PriceMapper {

    public Price mapToPrice(PriceDTO priceDTO, TennisCourt tennisCourt) {
        Price price = new Price();
        price.setSeason(priceDTO.getSeason());
        price.setPeriodOfDay(priceDTO.getPeriodOfDay());
        price.setPrice(priceDTO.getPrice());
        price.setTennisCourt(tennisCourt);
        return price;
    }
}
