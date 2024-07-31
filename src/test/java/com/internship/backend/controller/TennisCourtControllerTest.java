package com.internship.backend.controller;

import com.internship.backend.dto.TennisCourtDTO;
import com.internship.backend.exceptions.TennisCourtAlreadyExistsException;
import com.internship.backend.exceptions.TennisCourtDoesNotExistsException;
import com.internship.backend.model.TennisCourt;
import com.internship.backend.service.TennisCourtService;
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

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
class TennisCourtControllerTest {

    @InjectMocks
    private TennisCourtController tennisCourtController;

    @Mock
    private TennisCourtService tennisCourtService;

    private MockMvc mockMvc;
    private TennisCourt tennisCourt;
    private List<TennisCourt> tennisCourts;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(tennisCourtController).build();

        tennisCourt = createDefaultTennisCourt();

        tennisCourts = List.of(tennisCourt);
    }

    private TennisCourt createDefaultTennisCourt() {
        return TennisCourt.builder()
                .id(1)
                .name("tennisCourt")
                .details("details")
                .build();
    }

    @Test
    void shouldAddTennisCourt() throws Exception {
        when(tennisCourtService.addTennisCourt(any(TennisCourtDTO.class))).thenReturn(tennisCourt);

        mockMvc.perform(post("/api/tennisCourts/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"tennisCourt\",\"details\":\"details\",\"locationId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(tennisCourt.getName())))
                .andExpect(jsonPath("$.details", is(tennisCourt.getDetails())));

        verify(tennisCourtService).addTennisCourt(any(TennisCourtDTO.class));
    }

    @Test
    void shouldThrowErrorWhenAddingDuplicateTennisCourt() throws Exception {
        when(tennisCourtService.addTennisCourt(any(TennisCourtDTO.class))).thenThrow(TennisCourtAlreadyExistsException.class);

        mockMvc.perform(post("/api/tennisCourts/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"tennisCourt\",\"details\":\"details\",\"locationId\":1}"))
                .andExpect(status().isConflict());

        verify(tennisCourtService).addTennisCourt(any(TennisCourtDTO.class));
    }

    @Test
    void shouldRetrieveAllTennisCourts() throws Exception {
        when(tennisCourtService.getAllTennisCourts()).thenReturn(tennisCourts);

        mockMvc.perform(get("/api/tennisCourts/getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(tennisCourt.getName())))
                .andExpect(jsonPath("$[0].details", is(tennisCourt.getDetails())));

        verify(tennisCourtService).getAllTennisCourts();
    }

    @Test
    void shouldUpdateTennisCourt() throws Exception {
        when(tennisCourtService.updateTennisCourt(anyInt(), any(TennisCourtDTO.class))).thenReturn(tennisCourt);

        mockMvc.perform(put("/api/tennisCourts/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"updatedTennisCourt\",\"details\":\"updatedDetails\",\"locationId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(tennisCourt.getName())))
                .andExpect(jsonPath("$.details", is(tennisCourt.getDetails())));

        verify(tennisCourtService).updateTennisCourt(anyInt(), any(TennisCourtDTO.class));
    }

    @Test
    void shouldThrowErrorWhenUpdatingNonExistentTennisCourt() throws Exception {
        when(tennisCourtService.updateTennisCourt(anyInt(), any(TennisCourtDTO.class))).thenThrow(TennisCourtDoesNotExistsException.class);

        mockMvc.perform(put("/api/tennisCourts/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"updatedTennisCourt\",\"details\":\"updatedDetails\",\"locationId\":1}"))
                .andExpect(status().isNotFound());

        verify(tennisCourtService).updateTennisCourt(anyInt(), any(TennisCourtDTO.class));
    }

    @Test
    void shouldDeleteTennisCourt() throws Exception {
        doNothing().when(tennisCourtService).deleteTennisCourt(anyInt());

        mockMvc.perform(delete("/api/tennisCourts/delete/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(tennisCourtService).deleteTennisCourt(anyInt());
    }

    @Test
    void shouldThrowErrorWhenUserNotFoundToDelete() throws Exception {
        doThrow(TennisCourtDoesNotExistsException.class).when(tennisCourtService).deleteTennisCourt(anyInt());

        mockMvc.perform(delete("/api/tennisCourts/delete/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(tennisCourtService).deleteTennisCourt(anyInt());
    }
}