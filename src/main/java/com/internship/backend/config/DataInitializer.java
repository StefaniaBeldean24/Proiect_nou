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

import java.util.*;


@Configuration
public class DataInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    @Transactional
    public void init() {

        Optional<User> optionalUser = userRepository.findByUsername("test");

        if (optionalUser.isEmpty()) {
            Authority authority = Authority.builder()
                    .id(UUID.randomUUID().toString())
                    .name("ROLE_ADMIN")
                    .build();

            User user = User.builder()
                    .id(UUID.randomUUID().toString())
                    .username("test")
                    .password(passwordEncoder.encode("password"))
                    .email("test@yahoo.com")
                    .authorities(Set.of(authority))
                    .build();

            authorityRepository.save(authority);
            userRepository.save(user);
        }
    }
}