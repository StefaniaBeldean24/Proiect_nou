package com.internship.backend.mappper;

import com.internship.backend.dto.AuthorityDTO;
import com.internship.backend.exceptions.UserDoesNotExistException;
import com.internship.backend.model.Authority;
import com.internship.backend.model.User;
import com.internship.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthorityMapper {

    @Autowired
    private UserRepository userRepository;

    public Authority authorityMapper(AuthorityDTO authorityDTO) throws UserDoesNotExistException {
        Authority authority = new Authority();
        authority.setName(authority.getName());

        User user = userRepository.findById(authorityDTO.getUserId()).orElseThrow(()-> new UserDoesNotExistException("User does not exist"));
        authority.setUser(user);

        return authority;
    }
}
