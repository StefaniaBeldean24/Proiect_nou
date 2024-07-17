package com.internship.backend.config;

import com.internship.backend.model.Authority;
import com.internship.backend.model.User;
import com.internship.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UsernamePasswordAuthenticationProviderTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void authenticate_ValidCredentials() {
        String username = "test";
        String password = "password";
        String encodedPassword = "$2a$12$ww5wq5geTUUcKtzl.snWF.rOc62Fg./3/4HLkNJ8OjWeEu7cMpo3C";

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setAuthorities(Set.of(new Authority("ROLE_ADMIN")));

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        Authentication result = usernamePasswordAuthenticationProvider.authenticate(authentication);

        assertNotNull(result);
        assertEquals(username, result.getName());
        assertEquals(password, result.getCredentials());
        assertTrue(result.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Test
    void authenticate_InvalidPassword() {
        String username = "test";
        String password = "password";
        String encodedPassword = "$2a$12$ww5wq5geTUUcKtzl.snWF.rOc62Fg./3/4HLkNJ8OjWeEu7cMpo3Caaa";

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setAuthorities(Set.of(new Authority("ROLE_ADMIN")));

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(false);

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);

        assertThrows(BadCredentialsException.class, () -> usernamePasswordAuthenticationProvider.authenticate(authentication));
    }
}