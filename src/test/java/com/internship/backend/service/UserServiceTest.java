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
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static java.util.List.of;
import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
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
    }

    private User createDefaultUser() {
        return User.builder()
                .id(1)
                .username("test")
                .password("password")
                .email("email@yahoo.com")
                .authorities(new HashSet<>())
                .reservations(new ArrayList<>())
                .build();
    }

    private UserDTO createDefaultUserDTO() {
        return new UserDTO("ROLE_ADMIN", "userDTO", "password", "userDTO@yahoo.com");
    }

    private Authority createDefaultAuthority() {
        return Authority.builder()
                .name("ROLE_ADMIN")
                .user(user)
                .build();
    }

    @Test
    void shouldRegisterNewUser() throws EmailAlreadyExistsException, UserAlreadyExistsException {
        user = createDefaultUser();
        userDTO = createDefaultUserDTO();
        authority = createDefaultAuthority();
        user.getAuthorities().add(authority);

        when(userRepository.findByUsername("userDTO")).thenReturn(null);
        when(userRepository.findByEmail("userDTO@yahoo.com")).thenReturn(null);
        when(passwordEncoder.encode("password")).thenReturn("$2a$12$302xzRikiGdJmeistkERbu5r66ulhUPRL5v1lyNGL6kqWHTqRMDY6");
        when(userMapper.mapToUser(userDTO)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(authorityRepository.save(any(Authority.class))).thenReturn(authority);

        User savedUser = userService.register(userDTO);

        assertEquals("$2a$12$302xzRikiGdJmeistkERbu5r66ulhUPRL5v1lyNGL6kqWHTqRMDY6", savedUser.getPassword());
        verify(userRepository).save(any(User.class));
        verify(authorityRepository).save(any(Authority.class));
    }

    @Test
    void shouldRetrieveAllUsersFromProcedure() {
        user = createDefaultUser();
        users = of(user);

        when(userRepository.getAllUsersProcedure()).thenReturn(users);
        List<User> result = userService.getAllUsersProcedure();

        assertEquals(users, result);
        verify(userRepository).getAllUsersProcedure();
    }

    @Test
    void ShouldDleteUserByIdProcedure() {
        user = createDefaultUser();
        Integer userId = 1;

        doNothing().when(authorityRepository).deleteByUserId(userId);
        doNothing().when(userRepository).deleteUserByIdProcedure(userId);

        userService.deleteUserByIdProcedure(userId);

        verify(authorityRepository).deleteByUserId(userId);
        verify(userRepository).deleteUserByIdProcedure(userId);
    }

    @Test
    void shouldNotRegisterWithExistingUsername() {
        user = createDefaultUser();

        when(userRepository.findByUsername("test")).thenReturn(user);
        when(userMapper.mapToUser(userDTO)).thenReturn(user);

        assertThrows(UserAlreadyExistsException.class, () -> userService.register(userDTO));
    }

    @Test
    void shouldNotRegisterWithExistingEmail() {
        user = createDefaultUser();

        when(userRepository.findByEmail("email@yahoo.com")).thenReturn(user);
        when(userMapper.mapToUser(userDTO)).thenReturn(user);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.register(userDTO));
    }

    @Test
    void shouldRetrieveAllUsers() {
        user = createDefaultUser();
        users = of(user);

        when(userRepository.findAll()).thenReturn(users);
        List<User> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals(user, result.get(0));
    }

    @Test
    void shouldRetrieveUsersById() {
        user = createDefaultUser();

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        Optional<User> result = userService.getUserById(1);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void shouldUpdateUser() throws IdUserNotFoundException {
        user = createDefaultUser();

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO userDetailsDTO = new UserDTO("ROLE_USER", "newUser", "newPassword", "newEmail@yahoo.com");
        User updatedUser = userService.updateUser(1, userDetailsDTO);

        assertEquals("newUser", updatedUser.getUsername());
        assertEquals("newPassword", updatedUser.getPassword());
        assertEquals("newEmail@yahoo.com", updatedUser.getEmail());
    }

    @Test
    void shouldNotUpdateNonExistingUSer() {
        user = createDefaultUser();

        when(userRepository.findById(1)).thenReturn(empty());
        UserDTO userDetailsDTO = new UserDTO("ROLE_USER", "newUser", "newPassword", "newEmail@yahoo.com");

        assertThrows(IdUserNotFoundException.class, () -> userService.updateUser(1, userDetailsDTO));
    }

    @Test
    void shouldDeleteUser() throws UserDoesNotExistException {
        when(userRepository.existsById(1)).thenReturn(true);

        userService.deleteUser(1);

        verify(userRepository).deleteById(1);
    }

    @Test
    void shouldNotDeleteNonExistingUser() {
        when(userRepository.existsById(1)).thenReturn(false);

        assertThrows(UserDoesNotExistException.class, () -> userService.deleteUser(1));
    }
}