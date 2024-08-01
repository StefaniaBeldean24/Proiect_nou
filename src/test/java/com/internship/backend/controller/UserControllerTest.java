package com.internship.backend.controller;

import com.internship.backend.dto.UserDTO;
import com.internship.backend.exceptions.IdUserNotFoundException;
import com.internship.backend.exceptions.UserAlreadyExistsException;
import com.internship.backend.exceptions.UserDoesNotExistException;
import com.internship.backend.model.User;
import com.internship.backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static com.google.common.collect.ImmutableList.of;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;
    private User user;
    private List<User> users;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    private User createDefaultUser() {
        return User.builder()
                .id(1)
                .username("testUser")
                .email("test@example.com")
                .password("password")
                .build();
    }

    @Test
    void shouldCreateUser() throws Exception {
        user = createDefaultUser();
        when(userService.register(any(UserDTO.class))).thenReturn(user);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"role\":\"ROLE_USER\",\"username\":\"testUser\",\"password\":\"password\",\"email\":\"test@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));

        verify(userService).register(any(UserDTO.class));
    }

    @Test
    void shouldThrowErrorWhenAddingDuplicateUser() throws Exception {
        when(userService.register(any(UserDTO.class))).thenThrow(UserAlreadyExistsException.class);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"role\":\"ROLE_USER\",\"username\":\"testUser\",\"password\":\"password\",\"email\":\"test@example.com\"}"))
                .andExpect(status().isConflict());

        verify(userService).register(any(UserDTO.class));
    }

    @Test
    void getAllUsersProcedure() throws Exception {
        user = createDefaultUser();
        users = of(user);
        when(userService.getAllUsersProcedure()).thenReturn(users);

        mockMvc.perform(get("/api/users/getAllUsersProcedure")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(userService).getAllUsersProcedure();
    }

    @Test
    void deleteUserByIdProcedure() throws Exception {
        user = createDefaultUser();
        Integer userId = 1;
        doNothing().when(userService).deleteUserByIdProcedure(userId);

        mockMvc.perform(delete("/api/users/deleteUserByIdProcedure/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(userService).deleteUserByIdProcedure(userId);
    }

    @Test
    void shouldRetrieveAllUsers() throws Exception {
        user = createDefaultUser();
        users = List.of(user);
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users/getAllUsers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username", is(user.getUsername())))
                .andExpect(jsonPath("$[0].email", is(user.getEmail())));

        verify(userService).getAllUsers();
    }

    @Test
    void shouldRetrieveUsersById() throws Exception {
        user = createDefaultUser();
        when(userService.getUserById(anyInt())).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));

        verify(userService).getUserById(anyInt());
    }

    @Test
    void shouldUpdateUser() throws Exception {
        user = createDefaultUser();
        when(userService.updateUser(anyInt(), any(UserDTO.class))).thenReturn(user);

        mockMvc.perform(put("/api/users/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"role\":\"ROLE_USER\",\"username\":\"testUser\",\"password\":\"password\",\"email\":\"test@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));

        verify(userService).updateUser(anyInt(), any(UserDTO.class));
    }

    @Test
    void shouldThrowErrorWhenUpdatingNonExistentUser() throws Exception {
        when(userService.updateUser(anyInt(), any(UserDTO.class))).thenThrow(IdUserNotFoundException.class);

        mockMvc.perform(put("/api/users/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"role\":\"ROLE_USER\",\"username\":\"testUser\",\"password\":\"password\",\"email\":\"test@example.com\"}"))
                .andExpect(status().isConflict());

        verify(userService).updateUser(anyInt(), any(UserDTO.class));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(anyInt());

        mockMvc.perform(delete("/api/users/delete/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService).deleteUser(anyInt());
    }

    @Test
    void shouldThrowErrorWhenDeletingNonExistentUser() throws Exception {
        doThrow(UserDoesNotExistException.class).when(userService).deleteUser(anyInt());

        mockMvc.perform(delete("/api/users/delete/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(userService).deleteUser(anyInt());
    }
}