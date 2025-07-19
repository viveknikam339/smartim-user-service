package com.smartim.userservice.service;

import com.smartim.userservice.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    String register(RegisterRequest request);

    String login(LoginRequest request);

    UserDto getUserByEmail(String email);

    UserDto getUserByUserName(String email);

    List<UserDto> getUsersByRole(String role);

    UserDto updateUserProfile(String userName, UpdateUserRequest request);

    UserDto updateUserStatus(String userName);

    List<UserDto> getUsersWithFilters(String email, String role, Boolean status);

    void updateRole(String userName, String role, String updatedBy);

    void deleteUser(String userName);

    String resetUserPassword(ResetPasswordRequest resetPasswordRequest);
}
