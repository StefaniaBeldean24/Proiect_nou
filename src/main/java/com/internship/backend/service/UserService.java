package com.internship.backend.service;

import com.internship.backend.dto.UserDTO;
import com.internship.backend.exceptions.EmailAlreadyExistsException;
import com.internship.backend.exceptions.UserAlreadyExistsException;
import com.internship.backend.exceptions.UserDoesNotExistException;
import com.internship.backend.model.Authority;
import com.internship.backend.model.Reservation;
import com.internship.backend.model.User;
import com.internship.backend.repository.AuthorityRepository;
import com.internship.backend.repository.ReservationRepository;
import com.internship.backend.repository.UserRepository;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IdGeneratorService idGeneratorService;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private ReservationRepository reservationRepository;

    @Transactional
    public User register(User user) throws EmailAlreadyExistsException, UserAlreadyExistsException {

        validateRegisterData(user);
        user.setId(idGeneratorService.getCurrentId());
        String hashPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);

        User savedUser = userRepository.save(user);

        Set<Authority> authorities = user.getAuthorities();
        Iterator<Authority> iterator = authorities.iterator();
        authorityRepository.save(iterator.next());

        return savedUser;
    }

    private void validateRegisterData(User user) throws UserAlreadyExistsException, EmailAlreadyExistsException{
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
    }

    public User parse(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());

        Authority authority = resolveAuthority(userDTO.getRole());
        user.setAuthorities(Collections.singleton(authority));

        return user;
    }

    private Authority resolveAuthority(String roleName) {
        Optional<Authority> optionalAuthority = authorityRepository.findByName(roleName);
        if (optionalAuthority.isPresent()) {
            return optionalAuthority.get();
        } else {
            Authority newAuthority = new Authority(roleName);
            newAuthority.setId(idGeneratorService.getCurrentId());
            return authorityRepository.save(newAuthority);
        }
    }


    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();

        users.forEach(user -> {
            List<Reservation> reservations = reservationRepository.findByUserId(user.getId());
            user.setReservations(reservations);
        });
        logger.info("Number of users: " + users.size());
        return users;
    }


    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }


    public User updateUser(Integer id, User userDetails) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setUsername(userDetails.getUsername());
            user.setPassword(userDetails.getPassword());
            user.setEmail(userDetails.getEmail());
            user.setAuthorities(userDetails.getAuthorities());
            return userRepository.save(user);
        } else {
            throw new RuntimeException("User not found with id " + id);
        }
    }

    public void deleteUser(Integer id) throws UserDoesNotExistException {
        if(!userRepository.existsById(id)){
            throw new UserDoesNotExistException("User does not exisit");
        }
        userRepository.deleteById(id);
    }
}

