package com.smartim.userservice.service;

import com.smartim.userservice.dto.LoginRequest;
import com.smartim.userservice.dto.RegisterRequest;
import com.smartim.userservice.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    String register(RegisterRequest request);

    String login(LoginRequest request);

    UserDto getUserByEmail(String email);

    List<UserDto> getUsersByRole(String role);
}
