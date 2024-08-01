package com.internship.backend.service;

import com.internship.backend.dto.UserDTO;
import com.internship.backend.exceptions.EmailAlreadyExistsException;
import com.internship.backend.exceptions.IdUserNotFoundException;
import com.internship.backend.exceptions.UserAlreadyExistsException;
import com.internship.backend.exceptions.UserDoesNotExistException;
import com.internship.backend.mappper.UserMapper;
import com.internship.backend.model.Authority;
import com.internship.backend.model.User;
import com.internship.backend.repository.AuthorityRepository;
import com.internship.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Optional.ofNullable;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserMapper userMapper = new UserMapper();

    public User register(UserDTO userDTO) throws EmailAlreadyExistsException, UserAlreadyExistsException {
        User user = userMapper.mapToUser(userDTO);
        registerValidation(user);

        String hashPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);

        User savedUser = userRepository.save(user);

        Set<Authority> authorities = user.getAuthorities();
        Iterator<Authority> iterator = authorities.iterator();
        authorityRepository.save(iterator.next());

        return savedUser;
    }

    public List<User> getAllUsersProcedure() {
        return userRepository.getAllUsersProcedure();
    }

    public void deleteUserByIdProcedure(Integer userId) {
        authorityRepository.deleteByUserId(userId);
        userRepository.deleteUserByIdProcedure(userId);
    }

    private void registerValidation(User user) throws UserAlreadyExistsException, EmailAlreadyExistsException {
        Optional<User> findByUsername = ofNullable(userRepository.findByUsername(user.getUsername()));
        Optional<User> findByEmail = ofNullable(userRepository.findByEmail(user.getEmail()));

        if (findByUsername.isPresent()) {
            throw new UserAlreadyExistsException("Username already exists");
        }

        if (findByEmail.isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    public User updateUser(Integer id, UserDTO userDetailsDTO) throws IdUserNotFoundException {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            setUsernamePasswordEmail(user, userDetailsDTO.getUsername(), userDetailsDTO.getPassword(), userDetailsDTO.getEmail());

            Authority authority = new Authority();
            authority.setName(userDetailsDTO.getRole());
            user.setAuthorities(Set.of(authority));

            return userRepository.save(user);
        } else {
            throw new IdUserNotFoundException("User not found with id " + id);
        }
    }

    public void setUsernamePasswordEmail(User user, String username, String password, String email) {
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
    }

    public void resetAutoIncrement() {
        if (userRepository.count() == 0) {
            userRepository.resetAutoIncrementId();
        }
    }

    public void deleteUser(Integer id) throws UserDoesNotExistException {
        if (!userRepository.existsById(id)) {
            throw new UserDoesNotExistException("User does not exisit");
        }

        userRepository.deleteById(id);

        resetAutoIncrement();
    }
}

