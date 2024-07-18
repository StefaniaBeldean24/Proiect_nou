package com.internship.backend.service;

import com.internship.backend.dto.UserDTO;
import com.internship.backend.exceptions.EmailAlreadyExistsException;
import com.internship.backend.exceptions.IdUserNotFoundException;
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
    private AuthorityRepository authorityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private ReservationRepository reservationRepository;

    @Transactional
    public User register(User user) throws EmailAlreadyExistsException, UserAlreadyExistsException {

        registerValidation(user);
        String hashPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);

        User savedUser = userRepository.save(user);

        Set<Authority> authorities = user.getAuthorities();
        Iterator<Authority> iterator = authorities.iterator();
        authorityRepository.save(iterator.next());

        return savedUser;
    }

    private void registerValidation(User user) throws UserAlreadyExistsException, EmailAlreadyExistsException{

        Optional<User> findByUsername = Optional.ofNullable(userRepository.findByUsername(user.getUsername()));
        Optional<User> findByEmail = Optional.ofNullable(userRepository.findByEmail(user.getEmail()));

        if (findByUsername.isPresent()) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        if (findByEmail.isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
    }

    public User parse(UserDTO userDTO) {
        User user = User.builder()
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
                .email(userDTO.getEmail())
                .build();

        Set<Authority> authorities = new HashSet<>();
        Authority authority = new Authority();
        authority.setName(userDTO.getRole());
        authority.setUser(user);

        authorities.add(authority);
        user.setAuthorities(authorities);

        return user;
    }


    public List<User> getAllUsersWithReservation() {
        List<User> users = userRepository.findAll();

        users.forEach(user -> {
            List<Reservation> reservations = reservationRepository.findByUserId(user.getId());
            user.setReservations(reservations);
        });
        logger.info("Number of users: " + users.size());
        return users;
    }


    public List<User> getAllUsers() {
       return userRepository.findAll();
    }


    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }


    public User updateUser(Integer id, User userDetails) throws IdUserNotFoundException {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setUsername(userDetails.getUsername());
            user.setPassword(userDetails.getPassword());
            user.setEmail(userDetails.getEmail());
            user.setAuthorities(userDetails.getAuthorities());
            return userRepository.save(user);
        } else {
            throw new IdUserNotFoundException("User not found with id " + id);
        }
    }

    public void deleteUser(Integer id) throws UserDoesNotExistException {
        if(!userRepository.existsById(id)){
            throw new UserDoesNotExistException("User does not exisit");
        }
        userRepository.deleteById(id);

        if(userRepository.count() == 0){
            userRepository.resetAutoIncrementId();
        }
    }
}

