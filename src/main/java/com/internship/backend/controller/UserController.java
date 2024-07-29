package com.internship.backend.controller;

import com.internship.backend.dto.UserDTO;
import com.internship.backend.exceptions.EmailAlreadyExistsException;
import com.internship.backend.exceptions.IdUserNotFoundException;
import com.internship.backend.exceptions.UserAlreadyExistsException;
import com.internship.backend.exceptions.UserDoesNotExistException;
import com.internship.backend.model.User;
import com.internship.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.*;
import static java.util.Optional.ofNullable;


@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

    Logger Log = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/register")
    public ResponseEntity<User> createUser(@RequestBody UserDTO userDTO) {
        try{
            Log.info("User has been registered");
            return ok(userService.register(userDTO));
        } catch(UserAlreadyExistsException | EmailAlreadyExistsException e) {
            return status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        if(ofNullable(users).isPresent()) {
            Log.info("Get all users: ");
            return ok(users);
        } else {
            return notFound().build();
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<User>> getUserById(@PathVariable Integer id) {
        return ok(userService.getUserById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable Integer id, @RequestBody UserDTO userDTO) {
        try{
            Log.info("User has been updated");
            return ok(userService.updateUser(id, userDTO));
        } catch(IdUserNotFoundException e) {
            return status(HttpStatus.CONFLICT).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Integer id) {
        try{
            Log.info("Delete user with id " + id);
            userService.deleteUser(id);
            return ok().build();
        } catch(UserDoesNotExistException e) {
            return notFound().build();
        }
    }
}

