package com.internship.backend.controller;

import com.internship.backend.exceptions.UserDoesNotExistException;
import com.internship.backend.model.Authority;
import com.internship.backend.service.AuthorityService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;


@RestController
@RequestMapping("/api/authority")
public class AuthorityController {

    @Autowired
    private AuthorityService authorityService;

    @PostMapping("/addAuthority/{userId}")
    public Authority createAuthority(@PathVariable Integer userId, @RequestBody Authority authority) throws UserDoesNotExistException {
        return authorityService.createAuthority(userId, authority);
    }

    @GetMapping("/getAllAuthorities")
    public List<Authority> getAllAuthorities() {
        return authorityService.getAllAuthorities();
    }

    @DeleteMapping("/delete/{id}")
    public void deleteAuthority(@PathVariable Integer id) {
        authorityService.deleteAuthority(id);
    }
}
