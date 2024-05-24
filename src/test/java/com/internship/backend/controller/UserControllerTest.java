package com.internship.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.internship.backend.exceptions.UserAlreadyExistsException;
import com.internship.backend.exceptions.UserDoesNotExistException;
import com.internship.backend.model.Users;
import com.internship.backend.service.UserService;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.servlet.function.RequestPredicates.contentType;

import java.util.Arrays;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    //@Autowired
    //private UserRepository mockUserRepository;


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
        Users newUser = new Users(-1, "newuser", "newpassssssss", "new@example.com");
        Users registeredUser = new Users(1, "newuser", "newpasssssss", "new@example.com");
        when(userService.register(any(Users.class))).thenReturn(registeredUser);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registeredUser)))
                .andExpect(status().isOk());
        verify(userService).register(any(Users.class));
    }

    @Test
    public void testRegisterUser_UserExists() throws Exception {
        Users newUser = new Users(1, "newuser", "newpasssssss", "new@example.com");
        given(userService.register(any(Users.class))).willThrow(new UserAlreadyExistsException("User already exists"));

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isConflict());
    }


    @Test
    public void testLoginUser_Success() throws Exception {
        //Users loginDetails = new Users(-1, "user1", "passsssss1", null);
        Users loggedUser = new Users(1, "user1", "passsssss1", "user1@example.com");
        when(userService.login("user1", "passsssss1")).thenReturn(loggedUser);

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loggedUser)))
                .andExpect(status().isOk());
                //.andExpect(jsonPath("$.username").value("user1"));
        verify(userService).login("user1", "passsssss1");
    }

    @Test
    public void testLoginUser_Failed() throws Exception {
        Users loginDetails = new Users(1, "user10", "wrongpass", "user@gmail.com");
        when(userService.login("user10", "wrongpass")).thenReturn(null);

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDetails)))
                .andExpect(status().isUnauthorized());
        //verify(userService.login("user10", "wrongpass"));
    }


    @Test
    public void testUpdateUser_Success() throws Exception {
        Users updatedUser = new Users(1, "updated", "newPasssssss", "updated@example.com");
        given(userService.update(1, updatedUser)).willReturn(updatedUser);

        mockMvc.perform(put("/api/users/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("updated@example.com"));
    }

    @Test
    public void testUpdateUser_NotFound() throws Exception {
        Users updatedUser = new Users(1, "updated", "newPasssssss", "updated@example.com");
        given(userService.update(1, updatedUser)).willThrow(new UserDoesNotExistException("User not found"));

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

    @Test
    public void testValidationExceptionHandler_InvalidUSer() throws Exception {
        Users newUser = new Users(1, "abc", "newpasssssss", "new@example.com");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", Is.is("username must be between 4 and 10 characters")))
                .andExpect(MockMvcResultMatchers.content()
                                .contentType(MediaType.APPLICATION_JSON));
                //.andExpect(status().isBadRequest())
                //.andExpect(jsonPath("$.username").value("username must be between 4 and 10 characters"));
    }
}
