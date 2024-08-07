package com.internship.backend.service;

import com.internship.backend.dto.UserDTO;
import com.internship.backend.exceptions.UserAlreadyExistsException;
import com.internship.backend.exceptions.UserDoesNotExistException;
import com.internship.backend.model.Authority;
import com.internship.backend.model.User;
import com.internship.backend.repository.AuthorityRepository;
import com.internship.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static java.util.Collections.singleton;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User register(final UserDTO userDTO) throws UserAlreadyExistsException {
        return register(buildUser(userDTO));
    }

    public User register(final User user) {
        return userRepository.save(user);
    }

    private User buildUser(final UserDTO userDTO) throws UserAlreadyExistsException {
        checkUserExists(userDTO);

        var user = createUser(userDTO);

        var authority = createAuthority(userDTO);
        user.setAuthorities(singleton(authority));

        authorityRepository.save(authority);

        return user;
    }

    private void checkUserExists(final UserDTO userDTO) throws UserAlreadyExistsException {
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email already exists");
        }
    }

    private User createUser(final UserDTO userDTO) {
        var user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());
        return user;
    }

    private Authority createAuthority(final UserDTO userDTO) {
        var authority = new Authority();
        authority.setId(UUID.randomUUID().toString());
        authority.setName(userDTO.getRole());
        return authority;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(final String username, final UserDTO userDTO) throws UserDoesNotExistException {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserDoesNotExistException("User not found"));

        updateOldUser(user, userDTO);

        return userRepository.save(user);
    }

    public void updateOldUser(final User newUser, final UserDTO userDTO) {
        newUser.setUsername(userDTO.getUsername());
        var hashPassword = passwordEncoder.encode(userDTO.getPassword());
        newUser.setPassword(hashPassword);
        newUser.setEmail(userDTO.getEmail());

        var authority = authorityRepository.findByName(userDTO.getRole()).get();
        authority.setName(userDTO.getRole());

        newUser.setAuthorities(Set.of(authority));
    }

    public void deleteUser(final String username) throws UserDoesNotExistException {
        var userToDelete = userRepository.findByUsername(username).stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new UserDoesNotExistException("User not found"));

        userRepository.delete(userToDelete);
    }
}

