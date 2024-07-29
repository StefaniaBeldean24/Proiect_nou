package com.internship.backend.service;

import com.internship.backend.dto.PriceDTO;
import com.internship.backend.exceptions.PriceIdDoesNotExistException;
import com.internship.backend.exceptions.TennisCourtDoesNotExistsException;
import com.internship.backend.mappper.PriceMapper;
import com.internship.backend.model.Price;
import com.internship.backend.model.TennisCourt;
import com.internship.backend.repository.PriceRepository;
import com.internship.backend.repository.TennisCourtRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PriceServiceTest {

    @InjectMocks
    private PriceService priceService;

    @Mock
    private PriceRepository priceRepository;

    @Mock
    private TennisCourtRepository tennisCourtRepository;

    @Mock
    private PriceMapper priceMapper;

    private Price price;
    private PriceDTO priceDTO;
    private List<Price> prices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        price = Price.builder()
                .id(1)
                .price(100)
                .season("Summer")
                .periodOfDay("Morning")
                .build();

        priceDTO = new PriceDTO("Summer","Morning", 100, 1 );

        prices = List.of(price);
    }

    @Test
    void getAllPrices() {
        when(priceRepository.findAll()).thenReturn(prices);

        List<Price> result = priceService.getAllPrices();

        assertEquals(1, result.size());
        assertEquals(price, result.get(0));
    }

    @Test
    void addPrice() throws TennisCourtDoesNotExistsException, TennisCourtDoesNotExistsException {
        when(priceMapper.priceMapper(any(PriceDTO.class))).thenReturn(price);
        when(priceRepository.save(any(Price.class))).thenReturn(price);

        Price savedPrice = priceService.addPrice(priceDTO);

        assertEquals(price.getId(), savedPrice.getId());
        assertEquals(price.getPrice(), savedPrice.getPrice());
        assertEquals(price.getSeason(), savedPrice.getSeason());
        assertEquals(price.getPeriodOfDay(), savedPrice.getPeriodOfDay());
        verify(priceRepository).save(any(Price.class));
    }

    @Test
    void updatePrice() throws PriceIdDoesNotExistException {
        when(priceRepository.findById(1)).thenReturn(Optional.of(price));
        when(priceRepository.save(any(Price.class))).thenReturn(price);

        PriceDTO updatedPriceDTO = new PriceDTO("Winter","Evening", 100, 1);
        Price updatedPrice = priceService.update(1, updatedPriceDTO);

        assertEquals(updatedPriceDTO.getPrice(), updatedPrice.getPrice());
        assertEquals(updatedPriceDTO.getSeason(), updatedPrice.getSeason());
        assertEquals(updatedPriceDTO.getPeriodOfDay(), updatedPrice.getPeriodOfDay());
        verify(priceRepository).save(any(Price.class));
    }

    @Test
    void updatePriceThrowsExceptionWhenNotFound() {
        when(priceRepository.findById(1)).thenReturn(Optional.empty());

        PriceDTO updatedPriceDTO = new PriceDTO("Summer","Morning", 100, 1);

        assertThrows(PriceIdDoesNotExistException.class, () -> priceService.update(1, updatedPriceDTO));
    }

    @Test
    void deletePrice() {
        when(priceRepository.existsById(1)).thenReturn(true);

        priceService.delete(1);

        verify(priceRepository).deleteById(1);
    }

    @Test
    void deletePriceThrowsExceptionWhenNotFound() {
        when(priceRepository.existsById(1)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> priceService.delete(1));
    }
}