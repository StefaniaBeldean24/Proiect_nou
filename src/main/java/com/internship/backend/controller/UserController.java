package com.internship.backend.controller;

import com.internship.backend.exceptions.UserAlreadyExistsException;
import com.internship.backend.exceptions.UserDoesNotExistException;
import com.internship.backend.model.Users;
import com.internship.backend.service.AdminService;
import com.internship.backend.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;


@RestController
@RequestMapping("api/users")
public class UserController {

    Logger Log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;


    @GetMapping("/getAll")
    public ResponseEntity<List<Users>> getAll(){
        Optional<List<Users>> users = Optional.ofNullable(userService.getAllUsers());
        if(users.isPresent()) {
            Log.info("Get all" + userService.getAllUsers().toString());
            return ok(userService.getAllUsers());
        }
        else {
            Log.error("No users found");
            return notFound().build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Users> registerUser(@Valid @RequestBody Users users) {
        try{
            Optional<Users> newUser = Optional.ofNullable(userService.register(users));
            if(newUser.isPresent()) {
                Log.info("Registered user: {}", newUser.get());
                return ResponseEntity.ok().build();
            }
            else return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }catch (UserAlreadyExistsException e){
            Log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }catch (Exception e){
            Log.error("Wrong role!");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody Users loginDetails) {
        try{
            Optional<Users> users = Optional.ofNullable(userService.login(loginDetails.getUsername(), loginDetails.getPassword()));
            if (users.isPresent()) {
                Log.info("Logged in: " + users.toString());
                return ok(users);
            }
            else {
                Log.error("Login failed for user: " + loginDetails.toString());
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
            Log.info("Updating user: " + user.toString());
            return ok(updatedUser.get());
        }catch (UserDoesNotExistException e){
            Log.error("Error processing update "+ e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Users> deleteUser(@Valid @PathVariable("id") int userId) {
        try{
            Log.info("Deleting user: " + userId);
            userService.delete(userId);
            return ResponseEntity.ok().build();
        }catch (RuntimeException e){
            Log.error("Error processing delete "+ e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex){
        Map <String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error)-> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            Object rejectedValue = ((FieldError) error).getRejectedValue();
            errors.put(fieldName, errorMessage);
            Log.error("Validation error for field '{}': , Rejected value: '{}'", fieldName, rejectedValue);
        });
        return errors;
    }



    /*@PostMapping("/tennis-courts")
    public TennisCourt addTennisCourt(@RequestBody TennisCourt tennisCourt) {
        return adminService.addTennisCourt(tennisCourt);
    }

    @PostMapping("/price")
    public Price addPrice(@RequestBody PriceDTO priceDTO) {
        Price savedPrice = adminService.addPrice(priceDTO);
        //return adminService.addPrice(price);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPrice).getBody();
    }*/



}
