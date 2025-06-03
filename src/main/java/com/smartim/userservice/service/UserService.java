package com.smartim.userservice.service;

import com.smartim.userservice.dto.AuthResponse;
import com.smartim.userservice.dto.RegisterRequest;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    public AuthResponse register(RegisterRequest request);

    public AuthResponse login(RegisterRequest request);
}
