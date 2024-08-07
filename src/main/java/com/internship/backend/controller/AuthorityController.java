package com.internship.backend.controller;

import com.internship.backend.dto.AuthorityDTO;
import com.internship.backend.exceptions.AuthorityAlreadyExistsException;
import com.internship.backend.exceptions.AuthorityDoesNotExistException;
import com.internship.backend.exceptions.UserDoesNotExistException;
import com.internship.backend.model.Authority;
import com.internship.backend.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;


@RestController
@RequestMapping("/api/authority")
public class AuthorityController {

    @Autowired
    private AuthorityService authorityService;

    @PostMapping("/addAuthority/{userId}")
    public Authority createAuthority(@RequestBody AuthorityDTO authorityDTO) throws UserDoesNotExistException, AuthorityAlreadyExistsException {
        return authorityService.addAuthority(authorityDTO);
    }

    @GetMapping("/getAllAuthorities")
    public List<Authority> getAllAuthorities() {
        return authorityService.getAllAuthorities();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Authority> deleteAuthority(@PathVariable String userUsername, @PathVariable String role) {
        try {
            authorityService.deleteAuthority(userUsername, role);
            return ok().build();
        } catch (UserDoesNotExistException | AuthorityDoesNotExistException e) {
            return badRequest().build();
        }
    }
}
