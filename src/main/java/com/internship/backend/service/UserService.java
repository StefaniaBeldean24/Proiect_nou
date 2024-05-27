package com.internship.backend.service;

import com.internship.backend.exceptions.UserAlreadyExistsException;
import com.internship.backend.exceptions.UserDoesNotExistException;
import com.internship.backend.model.Users;
import com.internship.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Users update(int userId, Users updatedUser) throws UserDoesNotExistException {
        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users user = userRepository.findById(userId).orElseThrow(() -> new UserDoesNotExistException("User not found"));

        user.setRight(updatedUser.getRight());
        user.setUsername(updatedUser.getUsername());
        user.setPassword(updatedUser.getPassword());
        user.setEmail(updatedUser.getEmail());

        return userRepository.save(user);
    }

    public void delete(int userId) throws UserDoesNotExistException {
        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        //Users user = userRepository.findById(userId).orElseThrow(()->new UserDoesNotExistException("User not found"));

        if (!userRepository.existsById(userId))
            throw new RuntimeException("User does not exist");

        userRepository.deleteById(userId);

        if (userRepository.count() == 0) {
            userRepository.resetAutoIncrementId();
        }

        //if(isAdmin(userDetails, user)) userRepository.deleteById(userId);
        //else throw new SecurityException("User not authorized to delete this information");
    }

//    private boolean isAdmin(UserDetails userDetails, Users user){
//        return userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN") || user.getUsername().equals((userDetails.getUsername())));
//    }

    public Users register(Users users) throws UserAlreadyExistsException {
        if (userRepository.findByUsername(users.getUsername()) != null) {
            throw new UserAlreadyExistsException("Username already exists");//
        }
        if (userRepository.findByEmail(users.getEmail()) != null) {
            throw new UserAlreadyExistsException("Email already exists");
        }
        var savedUser = userRepository.save(users);
        //LOG.info("saved user: " + savedUser);
        return savedUser;
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
