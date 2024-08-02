package com.internship.backend.service;

import com.internship.backend.dto.PriceDTO;
import com.internship.backend.exceptions.PriceIdDoesNotExistException;
import com.internship.backend.exceptions.TennisCourtDoesNotExistsException;
import com.internship.backend.mappper.PriceMapper;
import com.internship.backend.model.Price;
import com.internship.backend.model.TennisCourt;
import com.internship.backend.repository.PriceRepository;
import com.internship.backend.repository.TennisCourtRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static java.util.List.of;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
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
    private TennisCourt tennisCourt;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Price createDefaultPrice() {
        return Price.builder()
                .id(1)
                .price(100)
                .season("Summer")
                .periodOfDay("Morning")
                .build();
    }

    private PriceDTO createDefaultPriceDTO() {
        return new PriceDTO("Summer", "Morning", 100, 1);
    }

    private TennisCourt createDefaultTennisCourt() {
        return TennisCourt.builder()
                .id(1)
                .name("TennisCourt")
                .details("Details about tennis court")
                .build();
    }

    @Test
    void shouldRetrieveAllPrices() {
        price = createDefaultPrice();
        prices = of(price);
        when(priceRepository.findAll()).thenReturn(prices);

        List<Price> result = priceService.getAllPrices();

        assertEquals(1, result.size());
        assertEquals(price, result.get(0));
    }

    @Test
    void shouldAddPrice() throws TennisCourtDoesNotExistsException {
        price = createDefaultPrice();
        priceDTO = createDefaultPriceDTO();
        tennisCourt = createDefaultTennisCourt();
        when(tennisCourtRepository.findById(priceDTO.getTennisCourtId())).thenReturn(ofNullable(tennisCourt));
        when(priceMapper.mapToPrice(eq(priceDTO), any(TennisCourt.class))).thenReturn(price);
        when(priceRepository.save(any(Price.class))).thenReturn(price);

        Price savedPrice = priceService.addPrice(priceDTO);

        assertNotNull(savedPrice);
        assertEquals(price.getPrice(), savedPrice.getPrice());
        verify(priceRepository).save(price);
    }

    @Test
    void shouldUpdatePrice() throws PriceIdDoesNotExistException, TennisCourtDoesNotExistsException {
        price = createDefaultPrice();
        tennisCourt = createDefaultTennisCourt();
        when(priceRepository.findById(1)).thenReturn(Optional.of(price));
        when(priceRepository.save(any(Price.class))).thenReturn(price);
        when(tennisCourtRepository.findById(tennisCourt.getId())).thenReturn(ofNullable(tennisCourt));

        PriceDTO updatedPriceDTO = new PriceDTO("Winter", "Evening", 100, 1);
        Price updatedPrice = priceService.update(1, updatedPriceDTO);

        assertEquals(updatedPriceDTO.getPrice(), updatedPrice.getPrice());
        assertEquals(updatedPriceDTO.getSeason(), updatedPrice.getSeason());
        assertEquals(updatedPriceDTO.getPeriodOfDay(), updatedPrice.getPeriodOfDay());
        verify(priceRepository).save(any(Price.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentPrice() {
        when(priceRepository.findById(1)).thenReturn(empty());

        PriceDTO updatedPriceDTO = new PriceDTO("Summer", "Morning", 100, 1);

        assertThrows(PriceIdDoesNotExistException.class, () -> priceService.update(1, updatedPriceDTO));
    }

    @Test
    void shouldDeletePrice() throws PriceIdDoesNotExistException {
        when(priceRepository.existsById(1)).thenReturn(true);

        priceService.delete(1);

        verify(priceRepository).deleteById(1);
    }

    @Test
    void shouldThrowExceptionPriceNotFoundToDelete() {
        when(priceRepository.existsById(1)).thenReturn(false);

        assertThrows(PriceIdDoesNotExistException.class, () -> priceService.delete(1));

        verify(priceRepository, never()).save(price);
    }
}