package com.internship.backend.service;

import com.internship.backend.dto.AuthorityDTO;
import com.internship.backend.exceptions.AuthorityAlreadyExistsException;
import com.internship.backend.exceptions.AuthorityDoesNotExistException;
import com.internship.backend.exceptions.UserDoesNotExistException;
import com.internship.backend.model.Authority;
import com.internship.backend.model.User;
import com.internship.backend.repository.AuthorityRepository;
import com.internship.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AuthorityService {

    Logger logger = LoggerFactory.getLogger(AuthorityService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    public Authority createAuthority(AuthorityDTO authorityDTO) throws UserDoesNotExistException, AuthorityAlreadyExistsException {
        var authorityExists = authorityRepository.findByName(authorityDTO.getRole()).isPresent();
        if (authorityExists) {
            throw new AuthorityAlreadyExistsException("This role already exists");
        }

        var user = userRepository.findByUsername(authorityDTO.getUserUsername())
                .orElseThrow(() -> new UserDoesNotExistException("User not found"));

        var authority = addRoleToAuthority(authorityDTO);
        addAuthorityToUser(authority, user);

        return createAuthority(authority);
    }

    public Authority addRoleToAuthority(AuthorityDTO authorityDTO) {
        var authority = new Authority();
        authority.setName(authorityDTO.getRole());
        return authority;
    }

    public void addAuthorityToUser(Authority authority, User user) {
        user.setAuthorities(Set.of(authority));
    }

    public Authority createAuthority(Authority authority) {
        return authorityRepository.save(authority);
    }

    public List<Authority> getAllAuthorities() {
        List<Authority> authorities = authorityRepository.findAll();
        logger.info("Authorities found: " + authorities.size());
        return authorities;
    }

    public void deleteAuthority(String userUsername, String role) throws AuthorityDoesNotExistException, UserDoesNotExistException {
        var user = userRepository.findByUsername(userUsername)
                .orElseThrow(() -> new UserDoesNotExistException("User not found"));

        var authorityToDelete = user.getAuthorities().stream()
                .filter(autority -> autority.getName().equals(role))
                .findFirst()
                .orElseThrow(() -> new AuthorityDoesNotExistException("Authority not found"));

        user.getAuthorities().remove(authorityToDelete);
        authorityRepository.delete(authorityToDelete);
    }
}
