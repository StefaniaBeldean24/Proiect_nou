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

    public Authority addAuthority(AuthorityDTO authorityDTO) throws UserDoesNotExistException, AuthorityAlreadyExistsException {
        checkAuthorityExists(authorityDTO);

        var user = findUser(authorityDTO);

        var authority = buildAuthority(authorityDTO);
        addAuthorityToUser(authority, user);

        return addAuthority(authority);
    }

    private void checkAuthorityExists(AuthorityDTO authorityDTO) throws AuthorityAlreadyExistsException {
        if (authorityRepository.findByName(authorityDTO.getRole()).isPresent()) {
            throw new AuthorityAlreadyExistsException("This role already exists");
        }
    }

    private User findUser(AuthorityDTO authorityDTO) throws UserDoesNotExistException {
        return userRepository.findByUsername(authorityDTO.getUserUsername())
                .orElseThrow(() -> new UserDoesNotExistException("User not found"));
    }

    public Authority buildAuthority(AuthorityDTO authorityDTO) {
        var authority = new Authority();
        authority.setName(authorityDTO.getRole());
        return authority;
    }

    public void addAuthorityToUser(Authority authority, User user) {
        user.setAuthorities(Set.of(authority));
    }

    public Authority addAuthority(Authority authority) {
        return authorityRepository.save(authority);
    }

    public List<Authority> getAllAuthorities() {
        List<Authority> authorities = authorityRepository.findAll();
        logger.info("Authorities found: " + authorities.size());
        return authorities;
    }

    public void deleteAuthority(String userUsername, String role) throws AuthorityDoesNotExistException, UserDoesNotExistException {
        final var user = userRepository.findByUsername(userUsername)
                .orElseThrow(() -> new UserDoesNotExistException("User not found"));

        final var authorityToDelete = user.getAuthorities().stream()
                .filter(autority -> autority.getName().equals(role))
                .findFirst()
                .orElseThrow(() -> new AuthorityDoesNotExistException("Authority not found"));

        user.getAuthorities().remove(authorityToDelete);
        authorityRepository.delete(authorityToDelete);
    }
}
