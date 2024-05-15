package com.internship.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.internship.backend.model.Users;
import com.internship.backend.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Optional;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void testGetAllUsers() throws Exception {
        Users user1 = new Users(1, "user1", "pass1", "user1@example.com");
        Users user2 = new Users(2, "user2", "pass2", "user2@example.com");
        given(userService.getAllUsers()).willReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/api/users/getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[1].username").value("user2"));
    }

    @Test
    public void testRegisterUser_Success() throws Exception {
        Users newUser = new Users(-1, "newuser", "newpass", "new@example.com");
        Users registeredUser = new Users(1, "newuser", "newpass", "new@example.com");
        given(userService.register(any(Users.class))).willReturn(registeredUser);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("newuser"));
    }

   /* @Test
    public void testRegisterUser_UserExists() throws Exception {
        Users newUser = new Users(1, "newuser", "newpass", "new@example.com");
        given(userService.register(any(Users.class))).willThrow(new RuntimeException("User already exists"));

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isBadRequest());
    }*/

    @Test
    public void testLoginUser_Success() throws Exception {
        Users loginDetails = new Users(-1, "user1", "pass1", null);
        Users loggedUser = new Users(1, "user1", "pass1", "user1@example.com");
        given(userService.login("user1", "pass1")).willReturn(loggedUser);

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"));
    }

    @Test
    public void testLoginUser_Failed() throws Exception {
        Users loginDetails = new Users(-1, "user1", "wrongpass", null);
        given(userService.login("user1", "wrongpass")).willReturn(null);

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDetails)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testUpdateUser_Success() throws Exception {
        Users updatedUser = new Users(1, "updatedUser", "newPass", "updated@example.com");
        given(userService.update(1, updatedUser)).willReturn(updatedUser);

        mockMvc.perform(put("/api/users/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("updated@example.com"));
    }

    @Test
    public void testUpdateUser_NotFound() throws Exception {
        Users updatedUser = new Users(1, "updatedUser", "newPass", "updated@example.com");
        given(userService.update(1, updatedUser)).willThrow(new RuntimeException("User not found"));

        mockMvc.perform(put("/api/users/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteUser_Success() throws Exception {
        mockMvc.perform(delete("/api/users/delete/1"))
                .andExpect(status().isOk());

        verify(userService).delete(1);
    }

    @Test
    public void testDeleteUser_NotFound() throws Exception {
        Mockito.doThrow(new RuntimeException("User does not exist")).when(userService).delete(1);

        mockMvc.perform(delete("/api/users/delete/1"))
                .andExpect(status().isNotFound());
    }
}
