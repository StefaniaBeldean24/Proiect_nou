package com.internship.backend.controller;


import com.internship.backend.model.Users;
import com.internship.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/getAll")
    public ResponseEntity<List<Users>> getAll(){
        List<Users> users = userService.getAllUsers();
        return ResponseEntity.ok(users);

    }

    @PostMapping("/register")
    public ResponseEntity<Users> registerUser(@Valid @RequestBody Users users) {
        Users newUsers = userService.register(users);
        return ResponseEntity.ok(newUsers);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Users loginDetails) {
        try{
            Users users = userService.login(loginDetails.getUsername(), loginDetails.getPassword());
            if (users != null) {
                return ResponseEntity.ok(users);
            }
            else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error processing request: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable("id") int userId, @RequestBody Users user) {
        try{
            Users updatedUser = userService.update(userId, user);
            return ResponseEntity.ok(updatedUser);
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Users> deleteUser(@PathVariable("id") int userId) {
        try{
            userService.delete(userId);
            return ResponseEntity.ok().build();
        }catch (RuntimeException e){
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
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

}
