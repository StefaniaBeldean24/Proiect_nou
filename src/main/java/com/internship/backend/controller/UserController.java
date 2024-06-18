package com.internship.backend.controller;

import com.internship.backend.dto.UserDTO;
import com.internship.backend.exceptions.UserAlreadyExistsException;
import com.internship.backend.exceptions.UserDoesNotExistException;
import com.internship.backend.model.Users;
import com.internship.backend.service.UserService;
import jakarta.validation.Valid;
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
public class UserController {

    Logger Log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/getAll")
    public ResponseEntity<List<Users>> getAll(){
        Optional<List<Users>> users = Optional.ofNullable(userService.getAllUsers());
        if(users.isPresent() && !users.get().isEmpty()) {
            Log.info("Get all" + userService.getAllUsers().toString());
            return ok(userService.getAllUsers());
        }
        else {
            Log.error("No users found");
            return notFound().build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Users> registerUser(@Valid @RequestBody UserDTO userDTO) {
        try {
            Users newUser = userService.register(userService.fromDTO(userDTO));
            //Optional<Users> newUser = Optional.ofNullable(userService.register(user));
            if (!Objects.isNull(newUser)) {
                Log.info("Registered user: {}", newUser);
                return ok(newUser);
            }
            else{
                Log.error("Registration failed");
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        } catch (UserAlreadyExistsException e) {
            Log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } catch (Exception e) {
            Log.error("Error during registration: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody Users loginDetails) {//dto
        try{
            Optional<Users> users = Optional.ofNullable(userService.login(loginDetails.getUsername(), loginDetails.getPassword()));
            if (users.isPresent()) {
                Log.info("Logged in: " + users.toString());
                return ok(users);
            }
            else {
                Log.error("Login failed for user: " + loginDetails);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
            }
        }catch (Exception e){
            Log.error("Error processing request " + e.getMessage());
            return ResponseEntity.badRequest().body("Error processing request: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable("id") int userId, @Valid @RequestBody Users user) {
        try{
            Optional<Users> updatedUser = Optional.ofNullable(userService.update(userId, user));
            Log.info("Updating user: " + user);
            return ok(updatedUser.get());
        }catch (UserDoesNotExistException e){
            Log.error("Error processing update "+ e.getMessage());
            return notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Users> deleteUser(@Valid @PathVariable("id") int userId) {
        try{
            Log.info("Deleting user: " + userId);
            userService.delete(userId);
            return ok().build();
        }catch (RuntimeException | UserDoesNotExistException e){
            Log.error("Error processing delete "+ e.getMessage());
            return notFound().build();
        }
    }

}
