package com.internship.backend.service;

import com.internship.backend.dto.AuthorityDTO;
import com.internship.backend.exceptions.UserDoesNotExistException;
import com.internship.backend.mappper.AuthorityMapper;
import com.internship.backend.model.Authority;
import com.internship.backend.model.User;
import com.internship.backend.repository.AuthorityRepository;
import com.internship.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorityService {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private UserRepository userRepository;

    AuthorityMapper authorityMapper = new AuthorityMapper();


    public Authority add(AuthorityDTO authorityDTO) throws UserDoesNotExistException {
        User user = userRepository.findById(authorityDTO.getUserId())
                .orElseThrow(() -> new UserDoesNotExistException("User does not exist"));
        Authority authority = authorityMapper.mapToAuthority(authorityDTO, user);
        authorityRepository.save(authority);
        return authority;
    }

    public void resetAutoIncrement() {
        if (authorityRepository.count() == 0) {
            authorityRepository.resetAutoIncrementId();
        }
    }

    public void delete(int authorityId){
        if (!authorityRepository.existsById(authorityId)) {
            throw new EntityNotFoundException("User not found");
        }

        authorityRepository.deleteById(authorityId);

        resetAutoIncrement();
    }

    public List<Authority> getAllAuthorities() {
        return authorityRepository.findAll();
    }
}
