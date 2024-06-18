package com.internship.backend.service;

import com.internship.backend.model.Authority;
import com.internship.backend.repository.AuthorityRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorityService {

    @Autowired
    private AuthorityRepository authorityRepository;


    public Authority add(Authority authority){
        authorityRepository.save(authority);
        return authority;
    }

    public void delete(int authorityId){

        if(!authorityRepository.existsById(authorityId))
            throw new EntityNotFoundException("User not found");

        authorityRepository.deleteById(authorityId);

        if (authorityRepository.count() == 0){
            authorityRepository.resetAutoIncrementId();
        }
    }

    public List<Authority> getAllAuthorities(){
        return authorityRepository.findAll();
    }
}
