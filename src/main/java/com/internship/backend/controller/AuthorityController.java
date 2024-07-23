package com.internship.backend.controller;

import com.internship.backend.model.Authority;
import com.internship.backend.service.AuthorityService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;


@RestController
@RequestMapping("/api/authority")
public class AuthorityController {

    @Autowired
    private AuthorityService authorityService;

    Logger Log = LoggerFactory.getLogger(AuthorityController.class);

    @PostMapping("/addAuthority/{userId}")
    public ResponseEntity<Authority> createAuthority(@RequestBody Authority authority) {
        return ok(authorityService.add(authority));
    }

    @GetMapping("/getAllAuthorities")
    public ResponseEntity<List<Authority>> getAllAuthorities() {
        return ok(authorityService.getAllAuthorities());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Authority> deleteAuthority(@PathVariable Integer id) {
        try{
            authorityService.delete(id);
            return ok().build();
        }catch(Exception e){
            Log.error("Error processing delete ", e.getMessage());
            return notFound().build();
        }

    }
}
