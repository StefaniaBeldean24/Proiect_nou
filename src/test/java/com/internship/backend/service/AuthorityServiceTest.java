package com.internship.backend.service;

import com.internship.backend.dto.AuthorityDTO;
import com.internship.backend.exceptions.UserDoesNotExistException;
import com.internship.backend.mappper.AuthorityMapper;
import com.internship.backend.model.Authority;
import com.internship.backend.repository.AuthorityRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthorityServiceTest {

    @InjectMocks
    private AuthorityService authorityService;

    @Mock
    private AuthorityRepository authorityRepository;

    @Mock
    private AuthorityMapper authorityMapper;

    private Authority authority;
    private AuthorityDTO authorityDTO;
    private List<Authority> authorities;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authority = Authority.builder()
                .id(1)
                .name("ROLE_USER")
                .build();

        authorityDTO = new AuthorityDTO("ROLE_USER", 1);

        authorities = List.of(authority);
    }

    @Test
    void add() throws UserDoesNotExistException {
        when(authorityMapper.authorityMapper(any(AuthorityDTO.class))).thenReturn(authority);
        when(authorityRepository.save(any(Authority.class))).thenReturn(authority);

        Authority savedAuthority = authorityService.add(authorityDTO);

        assertEquals("ROLE_USER", savedAuthority.getName());
        verify(authorityRepository).save(any(Authority.class));
    }

    @Test
    void delete() {
        when(authorityRepository.existsById(1)).thenReturn(true);

        authorityService.delete(1);

        verify(authorityRepository).deleteById(1);
    }

    @Test
    void deleteThrowsExceptionWhenAuthorityNotFound() {
        when(authorityRepository.existsById(1)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> authorityService.delete(1));
    }

    @Test
    void getAllAuthorities() {
        when(authorityRepository.findAll()).thenReturn(authorities);

        List<Authority> result = authorityService.getAllAuthorities();

        assertEquals(1, result.size());
        assertEquals(authority, result.get(0));
    }
}