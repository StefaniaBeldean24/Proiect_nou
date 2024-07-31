package com.internship.backend.mappper;

import com.internship.backend.dto.UserDTO;
import com.internship.backend.model.Authority;
import com.internship.backend.model.User;

import java.util.HashSet;
import java.util.Set;

public class UserMapper {

    public User mapToUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        Set<Authority> authorities = new HashSet<>();
        Authority authority = new Authority();
        authority.setName(userDTO.getRole());
        authority.setUser(user);
        authorities.add(authority);
        user.setAuthorities(authorities);
        return user;
    }
}
