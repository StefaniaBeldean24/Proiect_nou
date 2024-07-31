package com.internship.backend.service;

import com.internship.backend.dto.AuthorityDTO;
import com.internship.backend.exceptions.UserDoesNotExistException;
import com.internship.backend.model.Authority;
import com.internship.backend.model.User;
import com.internship.backend.repository.AuthorityRepository;
import com.internship.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class AuthorityServiceTest {

    @InjectMocks
    private AuthorityService authorityService;

    @Mock
    private AuthorityRepository authorityRepository;

    @Mock
    private UserRepository userRepository;

    private Authority authority;
    private AuthorityDTO authorityDTO;
    private List<Authority> authorities;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authority = createDefaultAuthority();
        authorityDTO = createDefaultAuthorityDTO();
        authorities = of(authority);
        user = new User();
    }

    private Authority createDefaultAuthority() {
        return Authority.builder()
                .id(1)
                .name("ROLE_USER")
                .build();
    }

    private AuthorityDTO createDefaultAuthorityDTO() {
        return new AuthorityDTO("ROLE_USER", 1);
    }

    @Test
    void shouldAddAuthority() throws UserDoesNotExistException {
        //Define the behaviour of the mock userRepository and authorityRepository
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        //call the add method
        Authority savedAuthority = authorityService.add(authorityDTO);

        //Check that the returned authority object has the expected properties
        assertNotNull(savedAuthority); //The returned authority should not be null
        assertEquals("ROLE_USER", savedAuthority.getName()); //The role should match the one in authorityDTO
        verify(authorityRepository).save(any(Authority.class)); //Verify that save method was called in the authority object
    }

    @Test
    void shouldThrowExceptionWhenAddingAnAuthorityForANonExistingUser() {
        //this method should throw UserDoesNotExistException when we want to add authority for a user that does not exist
        //mock userRepository.findById to throw exception
        when(userRepository.findById(10)).thenReturn(empty());

        assertThrows(UserDoesNotExistException.class, ()-> {
            authorityService.add(authorityDTO);
        });

        //verify that the method was never called since the user does not exist
        verify(authorityRepository, never()).save(any(Authority.class));
    }

    @Test
    void shouldDeleteExistingAuthority() {
        when(authorityRepository.existsById(1)).thenReturn(true);

        authorityService.delete(1);

        verify(authorityRepository).deleteById(1);
    }

    @Test
    void shouldThrowExceptionWhenAuthorityNotFoundToDelete() {
        //mock the autorityRepository to throw exception
        when(authorityRepository.existsById(1)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> authorityService.delete(1));
    }

    @Test
    void shouldRetrieveAllAuthorities() {
        when(authorityRepository.findAll()).thenReturn(authorities);

        List<Authority> result = authorityService.getAllAuthorities();

        assertEquals(1, result.size());
        assertEquals(authority, result.get(0));
    }
}