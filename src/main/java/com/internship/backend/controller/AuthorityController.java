package com.internship.backend.controller;

import com.internship.backend.model.Authority;
import com.internship.backend.service.AuthorityService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/api/authority")
public class AuthorityController {

    @Autowired
    private AuthorityService authorityService;

    @PostMapping("/addAuthority/{userId}")
    public Authority createAuthority(@RequestBody Authority authority) {
        return authorityService.add(authority);
    }

    @GetMapping("/getAllAuthorities")
    public List<Authority> getAllAuthorities() {
        return authorityService.getAllAuthorities();
    }

    @DeleteMapping("/delete/{id}")
    public void deleteAuthority(@PathVariable Integer id) {
        try{
            authorityService.delete(id);
        }catch(Exception e){
        }

    }
}
