package com.internship.backend.service;

import com.couchbase.client.core.error.UserNotFoundException;
import com.internship.backend.dto.AuthorityDTO;
import com.internship.backend.exceptions.UserDoesNotExistException;
import com.internship.backend.model.Authority;
import com.internship.backend.model.Users;
import com.internship.backend.repository.AuthorityRepository;

import com.internship.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorityService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private IdGeneratorService idGeneratorService;

    Logger logger = LoggerFactory.getLogger(AuthorityService.class);

    public Authority createAuthority(Integer userId, Authority authority) throws UserDoesNotExistException {
        Users user = userRepository.findById(userId).orElseThrow(()->new UserDoesNotExistException("User not found"));
        authority.setId(idGeneratorService.getCurrentId());
        user.getAuthorities().add(authority);
        userRepository.save(user);
        return authorityRepository.save(authority);
    }

    public List<Authority> getAllAuthorities() {
        List<Authority> authorities = authorityRepository.findAll();
        logger.info("Authorities found: " + authorities.size());
        return authorities;
    }

    public void deleteAuthority(Integer id) {
        authorityRepository.deleteById(id);
    }
}
