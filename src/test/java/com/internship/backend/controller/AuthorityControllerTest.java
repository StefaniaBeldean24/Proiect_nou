package com.internship.backend.controller;

import com.internship.backend.dto.AuthorityDTO;
import com.internship.backend.exceptions.UserDoesNotExistException;
import com.internship.backend.model.Authority;
import com.internship.backend.service.AuthorityService;
import jakarta.persistence.EntityNotFoundException;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
class AuthorityControllerTest {

    @InjectMocks
    private AuthorityController authorityController;

    @Mock
    private AuthorityService authorityService;

    private MockMvc mockMvc;
    private Authority authority;
    private List<Authority> authorities;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authorityController).build();
    }

    private Authority createDefaultAuthority() {
        return Authority.builder()
                .id(1)
                .name("ROLE_USER")
                .build();
    }

    @Test
    void shouldCreateAuthority() throws Exception {
        authority = createDefaultAuthority();
        when(authorityService.add(any(AuthorityDTO.class))).thenReturn(authority);

        mockMvc.perform(post("/api/authority/addAuthority/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"ROLE_USER\",\"userId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(authority.getName())));

        verify(authorityService).add(any(AuthorityDTO.class));
    }

    @Test
    void shouldNotCreateAuthorityForNonExistingUser() throws Exception {
        when(authorityService.add(any(AuthorityDTO.class))).thenThrow(UserDoesNotExistException.class);

        mockMvc.perform(post("/api/authority/addAuthority/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"ROLE_USER\",\"userId\":1}"))
                .andExpect(status().isBadRequest());

        verify(authorityService).add(any(AuthorityDTO.class));
    }

    @Test
    void shouldRetireveAllAuthorities() throws Exception {
        authority = createDefaultAuthority();
        authorities = List.of(authority);
        when(authorityService.getAllAuthorities()).thenReturn(authorities);

        mockMvc.perform(get("/api/authority/getAllAuthorities")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(authority.getName())));

        verify(authorityService).getAllAuthorities();
    }

    @Test
    void shouldDeleteAuthority() throws Exception {
        doNothing().when(authorityService).delete(anyInt());

        mockMvc.perform(delete("/api/authority/delete/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(authorityService).delete(anyInt());
    }

    @Test
    void shouldNotDeleteAuthorityForNonExisitingUser() throws Exception {
        doThrow(EntityNotFoundException.class).when(authorityService).delete(anyInt());

        mockMvc.perform(delete("/api/authority/delete/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(authorityService).delete(anyInt());
    }
}