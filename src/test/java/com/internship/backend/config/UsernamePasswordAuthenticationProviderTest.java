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

    private static final String USERNAME = "test";
    private static final String ENCODED_PASSWORD = "$2a$12$ww5wq5geTUUcKtzl.snWF.rOc62Fg./3/4HLkNJ8OjWeEu7cMpo3C";
    private static final String PASSWORD = "password";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private void createUser(String username, String encodedPassword, boolean passwordMatches) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setAuthorities(Set.of(new Authority("ROLE_ADMIN")));

        when(userRepository.findByUsername(username)).thenReturn(user);
        when(passwordEncoder.matches(PASSWORD, encodedPassword)).thenReturn(passwordMatches);
    }

    @Test
    void shouldAuthenticateUserWithValidCredentials() {
        createUser(USERNAME, ENCODED_PASSWORD, true);

        Authentication authentication = new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD);
        Authentication result = usernamePasswordAuthenticationProvider.authenticate(authentication);

        assertNotNull(result);
        assertEquals(USERNAME, result.getName());
        assertEquals(PASSWORD, result.getCredentials());
        assertTrue(result.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Test
    void shouldNotAuthenticateWithInvalidPassword() {
        String encodedPassword = ENCODED_PASSWORD + "aaa";

        createUser(USERNAME, encodedPassword, false);

        Authentication authentication = new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD);

        assertThrows(BadCredentialsException.class, () -> usernamePasswordAuthenticationProvider.authenticate(authentication));
    }
}