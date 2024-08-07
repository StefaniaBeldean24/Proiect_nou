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

    public Authority addAuthority(final AuthorityDTO authorityDTO) throws UserDoesNotExistException, AuthorityAlreadyExistsException {
        checkAuthorityExists(authorityDTO);

        var user = findUser(authorityDTO);

        var authority = buildAuthority(authorityDTO);
        addAuthorityToUser(authority, user);

        return addAuthority(authority);
    }

    private void checkAuthorityExists(final AuthorityDTO authorityDTO) throws AuthorityAlreadyExistsException {
        if (authorityRepository.findByName(authorityDTO.getRole()).isPresent()) {
            throw new AuthorityAlreadyExistsException("This role already exists");
        }
    }

    private User findUser(final AuthorityDTO authorityDTO) throws UserDoesNotExistException {
        return userRepository.findByUsername(authorityDTO.getUserUsername())
                .orElseThrow(() -> new UserDoesNotExistException("User not found"));
    }

    public Authority buildAuthority(final AuthorityDTO authorityDTO) {
        var authority = new Authority();
        authority.setName(authorityDTO.getRole());
        return authority;
    }

    public void addAuthorityToUser(final Authority authority, final User user) {
        user.setAuthorities(Set.of(authority));
    }

    public Authority addAuthority(final Authority authority) {
        return authorityRepository.save(authority);
    }

    public List<Authority> getAllAuthorities() {
        List<Authority> authorities = authorityRepository.findAll();
        logger.info("Authorities found: " + authorities.size());
        return authorities;
    }

    public void deleteAuthority(final String userUsername, final String role) throws AuthorityDoesNotExistException, UserDoesNotExistException {
        var user = userRepository.findByUsername(userUsername)
                .orElseThrow(() -> new UserDoesNotExistException("User not found"));

        var authorityToDelete = user.getAuthorities().stream()
                .filter(authority -> authority.getName().equals(role))
                .findFirst()
                .orElseThrow(() -> new AuthorityDoesNotExistException("Authority not found"));

        user.getAuthorities().remove(authorityToDelete);
        authorityRepository.delete(authorityToDelete);
    }
}
