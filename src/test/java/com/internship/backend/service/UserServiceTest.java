package com.internship.backend.service;

import com.internship.backend.dto.UserDTO;
import com.internship.backend.exceptions.EmailAlreadyExistsException;
import com.internship.backend.exceptions.IdUserNotFoundException;
import com.internship.backend.exceptions.UserAlreadyExistsException;
import com.internship.backend.exceptions.UserDoesNotExistException;
import com.internship.backend.mappper.UserMapper;
import com.internship.backend.model.Authority;
import com.internship.backend.model.User;
import com.internship.backend.repository.AuthorityRepository;
import com.internship.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthorityRepository authorityRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    private User user;
    private UserDTO userDTO;
    private Authority authority;
    private List<User> users;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = User.builder()
                .id(1)
                .username("test")
                .password("password")
                .email("email@yahoo.com")
                .authorities(new HashSet<>())
                .reservations(new ArrayList<>())
                .build();

        userDTO = new UserDTO("ROLE_ADMIN", "userDTO", "password", "userDTO@yahoo.com");

        authority = Authority.builder()
                .name("ROLE_ADMIN")
                .user(user)
                .build();

        user.getAuthorities().add(authority);
        users = List.of(user);
    }

    @Test
    void register() throws EmailAlreadyExistsException, UserAlreadyExistsException {
        when(userRepository.findByUsername("userDTO")).thenReturn(null);
        when(userRepository.findByEmail("userDTO@yahoo.com")).thenReturn(null);
        when(passwordEncoder.encode("password")).thenReturn("$2a$12$302xzRikiGdJmeistkERbu5r66ulhUPRL5v1lyNGL6kqWHTqRMDY6");
        when(userMapper.userMapper(any(UserDTO.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(authorityRepository.save(any(Authority.class))).thenReturn(authority);

        User savedUser = userService.register(userDTO);

        assertEquals("$2a$12$302xzRikiGdJmeistkERbu5r66ulhUPRL5v1lyNGL6kqWHTqRMDY6", savedUser.getPassword());
        verify(userRepository).save(any(User.class));
        verify(authorityRepository).save(any(Authority.class));
    }

    @Test
    void testRegisterWithExistingUsername() {
        when(userRepository.findByUsername("test")).thenReturn(user);
        when(userMapper.userMapper(any(UserDTO.class))).thenReturn(user);

        assertThrows(UserAlreadyExistsException.class, () -> userService.register(userDTO));
    }

    @Test
    void testRegisterWithExistingEmail() {
        when(userRepository.findByEmail("email@yahoo.com")).thenReturn(user);
        when(userMapper.userMapper(any(UserDTO.class))).thenReturn(user);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.register(userDTO));
    }

    @Test
    void getAllUsers() {
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals(user, result.get(0));
    }

    @Test
    void getUserById() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(1);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void updateUser() throws IdUserNotFoundException {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO userDetailsDTO = new UserDTO("ROLE_USER", "newUser", "newPassword", "newEmail@yahoo.com");
        User updatedUser = userService.updateUser(1, userDetailsDTO);

        assertEquals("newUser", updatedUser.getUsername());
        assertEquals("newPassword", updatedUser.getPassword());
        assertEquals("newEmail@yahoo.com", updatedUser.getEmail());
    }

    @Test
    void testUpdateUserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        UserDTO userDetailsDTO = new UserDTO("ROLE_USER", "newUser", "newPassword", "newEmail@yahoo.com");

        assertThrows(IdUserNotFoundException.class, () -> userService.updateUser(1, userDetailsDTO));
    }

    @Test
    void deleteUser() throws UserDoesNotExistException {
        when(userRepository.existsById(1)).thenReturn(true);

        userService.deleteUser(1);

        verify(userRepository).deleteById(1);
    }

    @Test
    void testDeleteUserNotFound() {
        when(userRepository.existsById(1)).thenReturn(false);

        assertThrows(UserDoesNotExistException.class, () -> userService.deleteUser(1));
    }
}