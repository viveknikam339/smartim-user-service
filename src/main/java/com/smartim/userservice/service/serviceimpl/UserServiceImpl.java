package com.smartim.userservice.service.serviceimpl;

import com.smartim.userservice.dto.AuthResponse;
import com.smartim.userservice.dto.RegisterRequest;
import com.smartim.userservice.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public AuthResponse register(RegisterRequest request) {
        return null;
    }

    @Override
    public AuthResponse login(RegisterRequest request) {
        return null;
    }
}
