package com.internship.backend.controller;

import com.internship.backend.dto.AuthorityDTO;
import com.internship.backend.exceptions.UserDoesNotExistException;
import com.internship.backend.model.Authority;
import com.internship.backend.service.AuthorityService;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.ResponseEntity.*;
import static java.util.Optional.ofNullable;


@RestController
@RequestMapping("/api/authority")
public class AuthorityController {

    @Autowired
    private AuthorityService authorityService;

    Logger Log = LoggerFactory.getLogger(AuthorityController.class);

    @PostMapping("/addAuthority/{userId}")
    public ResponseEntity<Authority> createAuthority(@RequestBody AuthorityDTO authorityDTO) {
        try {
            Log.info("Added new authority");
            return ok(authorityService.add(authorityDTO));
        } catch (UserDoesNotExistException e) {
            return status(BAD_REQUEST).build();
        }
    }

    @GetMapping("/getAllAuthorities")
    public ResponseEntity<List<Authority>> getAllAuthorities() {
        List<Authority> authorities = authorityService.getAllAuthorities();
        if (ofNullable(authorities).isPresent()) {
            Log.info("Get all authorities ");
            return ok(authorities);
        } else {
            return notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Authority> deleteAuthority(@PathVariable Integer id) {
        try {
            Log.info("Delete authority with id " + id);
            authorityService.delete(id);
            return ok().build();
        } catch (EntityNotFoundException e) {
            Log.error("Error processing delete ", e.getMessage());
            return notFound().build();
        }
    }
}
