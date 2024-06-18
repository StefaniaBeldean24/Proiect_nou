package com.internship.backend.service;

import com.internship.backend.exceptions.UserAlreadyExistsException;
import com.internship.backend.exceptions.UserDoesNotExistException;
import com.internship.backend.model.Users;
import com.internship.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Autowired
    @InjectMocks
    private UserService userService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /*@AfterEach
    void tearDown() {
        jdbcTemplate.update("DELETE FROM USERS");
    }*/

    @Test
    public void testRegisterUser_Success() throws UserAlreadyExistsException {
        Users user = new Users(0, "testuser", "password", "test@example.com");

        when(userRepository.findByUsername("testuser")).thenReturn(null);
        when(userRepository.save(any(Users.class))).thenReturn(user);

        Users createdUser = userService.register(user);

        assertNotNull(createdUser);
        assertEquals("testuser", createdUser.getUsername());
        assertEquals("password", createdUser.getPassword());
        assertEquals("test@example.com", createdUser.getEmail());
        verify(userRepository, times(1)).findByUsername("testuser");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testRegisterUser_UserAlreadyExists() {
        //Given
        Users user = new Users(0, "testuser", "password", "test@example.com");
        userRepository.save(user);

        //When
        when(userRepository.findByUsername("testuser")).thenReturn(user);

        // Then
        Exception exception = assertThrows(UserAlreadyExistsException.class, () -> userService.register(user));
        assertEquals("Username already exists", exception.getMessage());
        //verify(userRepository, times(1)).findByUsername("testuser");
        //verify(userRepository, times(0)).save(any(Users.class));
    }

    @Test
    public void testUpdateUser_Success() throws UserDoesNotExistException {
        Users existingUser = new Users(1, "existinguser", "password", "existing@example.com");
        Users updatedUser = new Users(1, "updateduser", "newpassword", "updated@example.com");

        userRepository.save(existingUser);
        userRepository.save(updatedUser);

        when(userRepository.findById(1)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(Users.class))).thenReturn(updatedUser);

        Users result = userService.update(1, updatedUser);

        assertNotNull(result);
        assertEquals("updateduser", result.getUsername());
        assertEquals("newpassword", result.getPassword());
        assertEquals("updated@example.com", result.getEmail());
        //verify(userRepository, times(1)).findById(1);
        //verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    public void testUpdateUser_UserNotFound() {
        Users updatedUser = new Users(1, "updateduser", "newpassword", "updated@example.com");
        userRepository.save(updatedUser);

        //when(userRepository.findById(1)).thenReturn(Optional.empty());
        //when(userRepository.findById(1)).thenReturn()

        Exception exception = assertThrows(UserDoesNotExistException.class, () -> userService.update(1, updatedUser));
        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(any(Users.class));
    }

    @Test
    public void testDeleteUser_Success() throws UserDoesNotExistException {
        when(userRepository.existsById(1)).thenReturn(true);

        userService.delete(1);

        verify(userRepository, times(1)).existsById(1);
        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    public void testDeleteUser_UserNotFound() {
        when(userRepository.existsById(1)).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> userService.delete(1));
        assertEquals("User does not exist", exception.getMessage());
        verify(userRepository, times(1)).existsById(1);
        verify(userRepository, times(0)).deleteById(anyInt());
    }

    @Test
    public void testLogin_Success() {
        Users user = new Users(1, "testuser", "password", "test@example.com");

        when(userRepository.findByUsername("testuser")).thenReturn(user);

        Users result = userService.login("testuser", "password");

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("password", result.getPassword());
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    public void testLogin_Failure_WrongPassword() {
        Users user = new Users(1, "testuser", "password", "test@example.com");

        when(userRepository.findByUsername("testuser")).thenReturn(user);

        Users result = userService.login("testuser", "wrongpassword");

        assertNull(result);
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    public void testLogin_Failure_UserNotFound() {
        when(userRepository.findByUsername("testuser")).thenReturn(null);

        Users result = userService.login("testuser", "password");

        assertNull(result);
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    public void testGetAllUsers() {
        List<Users> userList = List.of(
                new Users(1, "user1", "password1", "user1@example.com"),
                new Users(2, "user2", "password2", "user2@example.com")
        );

        when(userRepository.findAll()).thenReturn(userList);

        List<Users> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getUsername());
        assertEquals("user2", result.get(1).getUsername());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testRegisterUserWithExistingEmail() {
        Users existingUser = new Users(1, "existinguser", "password", "test@example.com");
        Users newUser = new Users(0, "newuser", "newpassword", "test@example.com");

        when(userRepository.findByUsername("newuser")).thenReturn(null);
        when(userRepository.findByEmail("test@example.com")).thenReturn(existingUser);

        Exception exception = assertThrows(UserAlreadyExistsException.class, () -> userService.register(newUser));
        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository, times(1)).findByUsername("newuser");
        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(userRepository, times(0)).save(any(Users.class));
    }
}
