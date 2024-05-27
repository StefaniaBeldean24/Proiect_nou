package com.internship.backend.service;

import com.internship.backend.exceptions.UserAlreadyExistsException;
import com.internship.backend.exceptions.UserDoesNotExistException;
import com.internship.backend.model.Users;
import com.internship.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Users update(int userId, Users updatedUser) throws UserDoesNotExistException {
        Users user = userRepository.findById(userId).orElseThrow(() -> new UserDoesNotExistException("User not found"));

        user.setRole(updatedUser.getRole());
        user.setUsername(updatedUser.getUsername());
        user.setPassword(updatedUser.getPassword());
        user.setEmail(updatedUser.getEmail());

        return userRepository.save(user);
    }

    public void delete(int userId) throws UserDoesNotExistException {
        if (!userRepository.existsById(userId))
            throw new RuntimeException("User does not exist");

        userRepository.deleteById(userId);

        if (userRepository.count() == 0) {
            userRepository.resetAutoIncrementId();
        }
    }

    public Users register(Users users) throws UserAlreadyExistsException {
        if (userRepository.findByUsername(users.getUsername()) != null) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        if (userRepository.findByEmail(users.getEmail()) != null) {
            throw new UserAlreadyExistsException("Email already exists");
        }
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        return userRepository.save(users);
    }

    public Users login(String username, String password) {
        Users users = userRepository.findByUsername(username);

        if (users != null && passwordEncoder.matches(password, users.getPassword())) {
            return users;
        }
        return null;
    }

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }
}
