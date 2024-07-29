package com.internship.backend.controller;

import com.internship.backend.dto.AuthorityDTO;
import com.internship.backend.exceptions.UserDoesNotExistException;
import com.internship.backend.model.Authority;
import com.internship.backend.service.AuthorityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
class AuthorityControllerTest {

    @InjectMocks
    private AuthorityController authorityController;

    @Mock
    private AuthorityService authorityService;

    private MockMvc mockMvc;
    private Authority authority;
    private AuthorityDTO authorityDTO;
    private List<Authority> authorities;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authorityController).build();

        authority = Authority.builder()
                .id(1)
                .name("ROLE_USER")
                .build();

        authorityDTO = new AuthorityDTO("ROLE_USER", 1);

        authorities = List.of(authority);
    }

    @Test
    void createAuthority() throws Exception {
        when(authorityService.add(any(AuthorityDTO.class))).thenReturn(authority);

        mockMvc.perform(post("/api/authority/addAuthority/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"ROLE_USER\",\"userId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(authority.getName())));

        verify(authorityService, times(1)).add(any(AuthorityDTO.class));
    }

    @Test
    void createAuthorityThrowsBadRequest() throws Exception {
        when(authorityService.add(any(AuthorityDTO.class))).thenThrow(UserDoesNotExistException.class);

        mockMvc.perform(post("/api/authority/addAuthority/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"ROLE_USER\",\"userId\":1}"))
                .andExpect(status().isBadRequest());

        verify(authorityService, times(1)).add(any(AuthorityDTO.class));
    }

    @Test
    void getAllAuthorities() throws Exception {
        when(authorityService.getAllAuthorities()).thenReturn(authorities);

        mockMvc.perform(get("/api/authority/getAllAuthorities")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(authority.getName())));

        verify(authorityService, times(1)).getAllAuthorities();
    }

    @Test
    void deleteAuthority() throws Exception {
        doNothing().when(authorityService).delete(anyInt());

        mockMvc.perform(delete("/api/authority/delete/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(authorityService, times(1)).delete(anyInt());
    }

    @Test
    void deleteAuthorityThrowsNotFound() throws Exception {
        doThrow(RuntimeException.class).when(authorityService).delete(anyInt());

        mockMvc.perform(delete("/api/authority/delete/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(authorityService, times(1)).delete(anyInt());
    }
}