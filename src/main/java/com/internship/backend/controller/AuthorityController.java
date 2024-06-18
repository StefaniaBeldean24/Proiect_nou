package com.internship.backend.controller;

import com.internship.backend.model.Authority;
import com.internship.backend.service.AuthorityService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/authority")
public class AuthorityController {

    @Autowired
    private AuthorityService authorityService;

    @GetMapping("/getAll")
    public ResponseEntity<List<Authority>> getAll(){
        Optional<List<Authority>> users = Optional.ofNullable(authorityService.getAllAuthorities());
        if(users.isPresent() && !users.get().isEmpty()) {
            return ok(authorityService.getAllAuthorities());
        }
        else {
            return notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Authority> deleteAuthority(@Valid @PathVariable("id") int authorityId) {
        try{
            authorityService.delete(authorityId);
            return ok().build();
        }catch (EntityNotFoundException e){
            return notFound().build();
        }
    }
}
