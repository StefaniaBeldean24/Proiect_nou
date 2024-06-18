package com.internship.backend.service;

import com.internship.backend.dto.UserDTO;
import com.internship.backend.exceptions.UserAlreadyExistsException;
import com.internship.backend.exceptions.UserDoesNotExistException;
import com.internship.backend.model.Authority;
import com.internship.backend.model.Users;
import com.internship.backend.repository.AuthorityRepository;
import com.internship.backend.repository.UserRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private AuthorityRepository authorityRepository;

    public Users register(Users user) throws UserAlreadyExistsException {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new UserAlreadyExistsException("Email already exists");
        }
        String hashPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);

        Users savedUser = userRepository.save(user);

        Set<Authority> authorities = user.getAuthorities();
        Iterator<Authority> iterator = authorities.iterator();

        authorityRepository.save(iterator.next());

        return savedUser;
    }

    public Users fromDTO(UserDTO userDTO) {
        Users user = Users.builder()
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
                .email(userDTO.getEmail())
                .build();

        Set<Authority> authorities = new HashSet<>();

        Authority authority = new Authority();
        authority.setName(userDTO.getRole());
        authority.setUser(user);

        authorities.add(authority);
        user.setAuthorities(authorities);

        return user;
    }

    public UserDTO toDTO(Users user){
        String role = user.getAuthorities().iterator().next().getName();

        return UserDTO.builder()
                .role(role)
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .build();
    }

    public Users update(int userId, Users updatedUser) throws UserDoesNotExistException {
        Users user = userRepository.findById(userId).orElseThrow(() -> new UserDoesNotExistException("User not found"));

        //user.setRole(updatedUser.getRole());
        user.setUsername(updatedUser.getUsername());
        user.setPassword(updatedUser.getPassword());
        user.setEmail(updatedUser.getEmail());

        return userRepository.save(user);
    }

    public void delete(int userId) throws UserDoesNotExistException {
        if (!userRepository.existsById(userId))
            throw new UserDoesNotExistException("User does not exist");

        userRepository.deleteById(userId);

        if (userRepository.count() == 0) {
            userRepository.resetAutoIncrementId();
        }
    }

    public Users login(String username, String password) {
        var users = userRepository.findByUsername(username);

        if (!ObjectUtils.isEmpty(users))
            return users;

        return null;
    }

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }
}
