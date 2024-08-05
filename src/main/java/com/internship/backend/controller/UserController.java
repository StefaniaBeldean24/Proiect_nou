package com.internship.backend.controller;

import com.internship.backend.dto.UserDTO;
import com.internship.backend.exceptions.UserAlreadyExistsException;
import com.internship.backend.exceptions.UserDoesNotExistException;
import com.internship.backend.model.User;
import com.internship.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> createUser(@RequestBody UserDTO userDTO) {

        try {
            userService.register(userDTO);
            return ok().build();
        } catch (UserAlreadyExistsException e) {
            return badRequest().build();
        }
    }

    @GetMapping("/getAllUsers")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String username, @RequestBody UserDTO userDTO) {
        try {
            userService.updateUser(username, userDTO);
            return ok().build();
        } catch (UserDoesNotExistException e) {
            return badRequest().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable String username) {
        try {
            userService.deleteUser(username);
            return ok().build();
        } catch (UserDoesNotExistException e) {
            return notFound().build();
        }
    }
}

