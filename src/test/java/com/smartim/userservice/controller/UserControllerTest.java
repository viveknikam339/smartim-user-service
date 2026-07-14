package com.smartim.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartim.userservice.dto.*;
import com.smartim.userservice.service.UserService;
import com.smartim.userservice.service.shared.RedisService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@WithMockUser(username = "testuser")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private RedisService redisService;

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
    }

    @Test
    void register_ShouldReturnAuthResponse() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        when(userService.register(any(RegisterRequest.class))).thenReturn("mockToken");

        mockMvc.perform(post("/api/users/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("mockToken"));
    }

    @Test
    void login_ShouldReturnAuthResponse() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        when(userService.login(any(LoginRequest.class))).thenReturn("mockToken");

        mockMvc.perform(post("/api/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mockToken"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void profile_ShouldReturnUserDto() throws Exception {
        when(redisService.get(anyString(), eq(UserDto.class))).thenReturn(null);
        when(userService.getUserByUserName("testuser")).thenReturn(userDto);

        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("testuser"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void updateProfile_ShouldReturnUpdatedUserDto() throws Exception {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        when(userService.updateUserProfile(eq("testuser"), any(UpdateUserRequest.class))).thenReturn(userDto);

        mockMvc.perform(put("/api/users/me")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("testuser"));
    }

    @Test
    void updateUserStatus_ShouldReturnUpdatedUserDto() throws Exception {
        when(userService.updateUserStatus("testuser")).thenReturn(userDto);

        mockMvc.perform(patch("/api/users/testuser/status")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("testuser"));
    }

    @Test
    void getUserById_ShouldReturnUserDto() throws Exception {
        when(redisService.get(anyString(), eq(UserDto.class))).thenReturn(null);
        when(userService.getUserByEmail("test@example.com")).thenReturn(userDto);

        mockMvc.perform(get("/api/users/test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void resetPassword_ShouldReturnSuccessMessage() throws Exception {
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        when(userService.resetUserPassword(any(ResetPasswordRequest.class))).thenReturn("Password reset successfully");

        mockMvc.perform(post("/api/users/resetPassword")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resetPasswordRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Password reset successfully"));
    }
}