package com.internship.backend.config;

import com.internship.backend.model.Authority;
import com.internship.backend.model.User;
import com.internship.backend.repository.AuthorityRepository;
import com.internship.backend.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static java.util.Collections.singleton;


@Configuration
public class DataInitializer {

    private static final String USERNAME = "test";
    private static final String EMAIL = "test@yahoo.com";
    private static final String PASSWORD = "password";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    @Transactional
    public void init() {

        if (userRepository.findByUsername(USERNAME).isEmpty()) {
            Authority authority = createAuthority();
            User user = createUser(authority);
            authorityRepository.save(authority);
            userRepository.save(user);
        }
    }

    private Authority createAuthority() {
        return Authority.builder()
                .id(UUID.randomUUID().toString())
                .name(ROLE_ADMIN)
                .build();
    }

    private User createUser(final Authority authority) {
        return User.builder()
                .id(UUID.randomUUID().toString())
                .username(USERNAME)
                .password(passwordEncoder.encode(PASSWORD))
                .email(EMAIL)
                .authorities(singleton(authority))
                .build();
    }
}