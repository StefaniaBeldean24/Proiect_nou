package com.internship.backend.config;

import com.internship.backend.model.Authority;
import com.internship.backend.model.Users;
import com.internship.backend.repository.AuthorityRepository;
import com.internship.backend.repository.UserRepository;
import com.internship.backend.service.IdGeneratorService;
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
    private IdGeneratorService idGeneratorService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    @Transactional
    public void init() {

        Optional<Authority> optionalAuthority = authorityRepository.findByName("ROLE_ADMIN");
        Authority adminAuthority;
        if (optionalAuthority.isEmpty()) {
            adminAuthority = new Authority("ROLE_ADMIN");
            adminAuthority.setId(idGeneratorService.getCurrentId());
            adminAuthority = authorityRepository.save(adminAuthority);
        } else {
            adminAuthority = optionalAuthority.get();
        }

        Optional<Users> optionalUser = userRepository.findByUsername("test");
        if (optionalUser.isEmpty()) {
            Users user = new Users();
            user.setId(idGeneratorService.getCurrentId());
            user.setUsername("test");
            user.setPassword(passwordEncoder.encode("password"));
            user.setEmail("test@yahoo.com");
            user.setAuthorities((Set.of(adminAuthority)));//Collections.singleton(adminAuthority)
            authorityRepository.save(adminAuthority);
            userRepository.save(user);
        }

    }
}