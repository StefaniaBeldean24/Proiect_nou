package com.internship.backend.service;

import com.internship.backend.model.Users;
import com.internship.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Users addUsers(Users users) {
        if(userRepository.findByUsername(users.getUsername()) != null) {
            throw new RuntimeException("User already exists");
        }
        return userRepository.save(users);
    }

    public Users register(Users users) {
        return userRepository.save(users);
    }

    public Users login(String username, String password) {
        Users users = userRepository.findByUsername(username);

        if ( users != null && users.getPassword().equals(password) ) {
            return users;
        }
        return null;
    }
}
