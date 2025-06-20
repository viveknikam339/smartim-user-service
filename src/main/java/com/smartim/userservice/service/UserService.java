package com.smartim.userservice.service;

import com.smartim.userservice.dto.LoginRequest;
import com.smartim.userservice.dto.RegisterRequest;
import com.smartim.userservice.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    String register(RegisterRequest request);

    String login(LoginRequest request);

    User getUserByEmail(String email);

    List<User> getUsersByRole(String role);
}
