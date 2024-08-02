package com.internship.backend.controller;

import com.internship.backend.dto.PriceDTO;
import com.internship.backend.exceptions.PriceIdDoesNotExistException;
import com.internship.backend.exceptions.TennisCourtDoesNotExistsException;
import com.internship.backend.model.Price;
import com.internship.backend.service.PriceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static com.google.common.collect.ImmutableList.of;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
class PriceControllerTest {
    @InjectMocks
    private PriceController priceController;

    @Mock
    private PriceService priceService;

    private MockMvc mockMvc;
    private Price price;
    private List<Price> prices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(priceController).build();
    }

    private Price createDefaultPrice() {
        return Price.builder()
                .id(1)
                .price(100)
                .season("Summer")
                .periodOfDay("Morning")
                .build();
    }

    @Test
    void shouldAddPrice() throws Exception {
        price = createDefaultPrice();
        when(priceService.addPrice(any(PriceDTO.class))).thenReturn(price);

        mockMvc.perform(post("/api/prices/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"season\":\"Summer\",\"periodOfDay\":\"Morning\",\"price\":100,\"tennisCourtId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price", is(price.getPrice())))
                .andExpect(jsonPath("$.season", is(price.getSeason())))
                .andExpect(jsonPath("$.periodOfDay", is(price.getPeriodOfDay())));

        verify(priceService).addPrice(any(PriceDTO.class));
    }

    @Test
    void shouldNotAddPriceForNonExistingTennisCourt() throws Exception {
        when(priceService.addPrice(any(PriceDTO.class))).thenThrow(TennisCourtDoesNotExistsException.class);

        mockMvc.perform(post("/api/prices/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"season\":\"Summer\",\"periodOfDay\":\"Morning\",\"price\":100,\"tennisCourtId\":1}"))
                .andExpect(status().isBadRequest());

        verify(priceService).addPrice(any(PriceDTO.class));
    }

    @Test
    void shouldRetrieveAllPrices() throws Exception {
        price = createDefaultPrice();
        prices = of(price);
        when(priceService.getAllPrices()).thenReturn(prices);

        mockMvc.perform(get("/api/prices/getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].price", is(price.getPrice())))
                .andExpect(jsonPath("$[0].season", is(price.getSeason())))
                .andExpect(jsonPath("$[0].periodOfDay", is(price.getPeriodOfDay())));

        verify(priceService).getAllPrices();
    }

    @Test
    void shouldUpdatePrice() throws Exception {
        price = createDefaultPrice();
        when(priceService.update(anyInt(), any(PriceDTO.class))).thenReturn(price);

        mockMvc.perform(put("/api/prices/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"season\":\"Winter\",\"periodOfDay\":\"Evening\",\"price\":150,\"tennisCourtId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price", is(price.getPrice())))
                .andExpect(jsonPath("$.season", is(price.getSeason())))
                .andExpect(jsonPath("$.periodOfDay", is(price.getPeriodOfDay())));

        verify(priceService).update(anyInt(), any(PriceDTO.class));
    }

    @Test
    void shouldNotUpdateNonExistingPrice() throws Exception {
        when(priceService.update(anyInt(), any(PriceDTO.class))).thenThrow(PriceIdDoesNotExistException.class);

        mockMvc.perform(put("/api/prices/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"season\":\"Winter\",\"periodOfDay\":\"Evening\",\"price\":150,\"tennisCourtId\":1}"))
                .andExpect(status().isNotFound());

        verify(priceService).update(anyInt(), any(PriceDTO.class));
    }

    @Test
    void shouldDeletePrice() throws Exception {
        doNothing().when(priceService).delete(anyInt());

        mockMvc.perform(delete("/api/prices/delete/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(priceService).delete(anyInt());
    }

    @Test
    void shouldNotDeleteNonExistentPrice() throws Exception {
        doThrow(PriceIdDoesNotExistException.class).when(priceService).delete(anyInt());

        mockMvc.perform(delete("/api/prices/delete/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(priceService).delete(anyInt());
    }
}