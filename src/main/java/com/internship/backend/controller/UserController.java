package com.internship.backend.controller;


import com.internship.backend.model.Users;
import com.internship.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public ResponseEntity<Users> addUser(@RequestBody Users user) {
        try{
            Users newUser = userService.addUsers(user);
            return ResponseEntity.ok(newUser);
        }catch(RuntimeException e){
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("register")
    public ResponseEntity<Users> registerUser(@RequestBody Users users) {
        Users newUsers = userService.register(users);
        return ResponseEntity.ok(newUsers);
    }

    @PostMapping("login")
    public ResponseEntity<Users> loginUser(@RequestBody Users loginDetails) {
        Users users = userService.login(loginDetails.getUsername(), loginDetails.getPassword());
        if (users != null) {
            return ResponseEntity.ok(users);
        }
        return ResponseEntity.ok(null);
    }

}
