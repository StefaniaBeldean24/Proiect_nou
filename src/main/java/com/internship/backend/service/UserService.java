package com.internship.backend.service;

import com.internship.backend.model.Users;
import com.internship.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Users update(int userId, Users updatedUser) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(updatedUser.getUsername());
        user.setPassword(updatedUser.getPassword());
        user.setEmail(updatedUser.getEmail());

        return userRepository.save(user);
    }

    public void delete(int userId) {
        if (!userRepository.existsById(userId))
            throw new RuntimeException("User does not exist");

        userRepository.deleteById(userId);

    if (userRepository.count() == 0) {
        userRepository.resetAutoIncrementId();
        }
    }

    public Users register(Users users) {
        if (userRepository.findByUsername(users.getUsername()) != null) {
            throw new RuntimeException("User already exists");
        }
        if (userRepository.findByEmail(users.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }
        return userRepository.save(users);
    }

    public Users login(String username, String password) {
        Users users = userRepository.findByUsername(username);

        if (users != null && users.getPassword().equals(password)) {
            return users;
        }
        return null;
    }

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }
}
