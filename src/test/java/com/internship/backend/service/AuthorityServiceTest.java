package com.internship.backend.service;

import com.internship.backend.exceptions.AuthorityDoesNotExistException;
import com.internship.backend.exceptions.UserDoesNotExistException;
import com.internship.backend.model.Authority;
import com.internship.backend.model.User;
import com.internship.backend.repository.AuthorityRepository;
import com.internship.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorityServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthorityRepository authorityRepository;

    @Mock
    private IdGeneratorService idGeneratorService;

    @InjectMocks
    private AuthorityService authorityService;

    @Test
    void createAuthority() throws UserDoesNotExistException {
        // arrange
        User user = new User();
        Authority authority = new Authority("ROLE_ADMIN");
        user.setId(1);
        user.setAuthorities(new HashSet<>());

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(idGeneratorService.getCurrentId()).thenReturn(1);
        when(authorityRepository.save(any(Authority.class))).thenReturn(authority);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // act
        Authority createdAuthority = authorityService.createAuthority(1, authority);

        // assert
        assertNotNull(createdAuthority);
        assertEquals("ROLE_ADMIN", createdAuthority.getName());
        assertTrue(user.getAuthorities().contains(authority));
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(user);
        verify(authorityRepository, times(1)).save(authority);
    }

    @Test
    void createAuthority_userNotFound() {
        //arrange
        Authority authority = new Authority("ROLE_ADMIN");
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        //act
        UserDoesNotExistException exception = assertThrows(UserDoesNotExistException.class, ()-> {
            authorityService.createAuthority(1, authority);
        });

        //assert
        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void getAllAuthorities() {
        //arrange
        List<Authority> authorities = new ArrayList<>();
        authorities.add(new Authority("ROLE_ADMIN"));
        authorities.add(new Authority("ROLE_USER"));

        when(authorityRepository.findAll()).thenReturn(authorities);

        //act
        List<Authority> result = authorityService.getAllAuthorities();

        //assert
        assertNotNull(result);
        assertEquals(result.size(), 2);
        assertTrue(result.contains(new Authority("ROLE_ADMIN")));
        assertTrue(result.contains(new Authority("ROLE_USER")));
        verify(authorityRepository, times(1)).findAll();
    }

    @Test
    void deleteAuthority() throws AuthorityDoesNotExistException {
        //arrange
        Authority authority = new Authority("ROLE_ADMIN");
        authority.setId(1);

        //act
        authorityService.deleteAuthority(1);

        //assert
        verify(authorityRepository, times(1)).deleteById(1);
    }
}