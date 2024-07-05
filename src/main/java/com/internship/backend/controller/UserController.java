package com.internship.backend.controller;

import com.internship.backend.dto.UserDTO;
import com.internship.backend.exceptions.UserAlreadyExistsException;
import com.internship.backend.exceptions.UserDoesNotExistException;
import com.internship.backend.model.Users;
import com.internship.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;


@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Users> createUser(@RequestBody UserDTO userDTO) {

        try{
            Users user = userService.fromDTO(userDTO);
            return ok(userService.register(user));
        }catch(UserAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

    }

    @GetMapping("/getAllUsers")
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Optional<Users> getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @PutMapping("/update/{id}")
    public Users updateUser(@PathVariable Integer id, @RequestBody Users user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Users> deleteUser(@PathVariable Integer id) {
        try{
            userService.deleteUser(id);
            return ok().build();
        }catch(UserDoesNotExistException e){
            return notFound().build();
        }

    }
}

