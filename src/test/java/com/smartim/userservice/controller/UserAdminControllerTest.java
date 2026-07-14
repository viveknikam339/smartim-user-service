package com.smartim.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartim.userservice.dto.UserDto;
import com.smartim.userservice.service.UserService;
import com.smartim.userservice.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserAdminController.class)
@WithMockUser(roles = "ADMIN")
class UserAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setUserName("testuser");
        userDto.setEmail("test@example.com");
        userDto.setRole("USER");
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() throws Exception {
        when(userService.getUsersWithFilters(any(), any(), any())).thenReturn(Collections.singletonList(userDto));

        mockMvc.perform(get("/api/admin/users/getUsers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].userName").value("testuser"));
    }

    @Test
    void getAllUsers_WithFilters_ShouldReturnFilteredListOfUsers() throws Exception {
        when(userService.getUsersWithFilters("test@example.com", "USER", true))
                .thenReturn(Collections.singletonList(userDto));

        mockMvc.perform(get("/api/admin/users/getUsers")
                        .param("email", "test@example.com")
                        .param("role", "USER")
                        .param("status", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userName").value("testuser"));
    }

    @Test
    void updateUserRole_ShouldReturnSuccessMessage() throws Exception {
        doNothing().when(userService).updateRole(anyString(), anyString(), anyString());

        mockMvc.perform(patch("/api/admin/users/testuser/role")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(content().string("User role updated"));
    }

    @Test
    void deleteUser_ShouldReturnSuccessMessage() throws Exception {
        doNothing().when(userService).deleteUser(anyString());

        mockMvc.perform(delete("/api/admin/users/testuser").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));
    }

    @Test
    void getUsersByRole_ShouldReturnListOfUsers() throws Exception {
        when(userService.getUsersByRole("USER")).thenReturn(Collections.singletonList(userDto));

        mockMvc.perform(get("/api/admin/users/role/USER"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].role").value("USER"));
    }
}