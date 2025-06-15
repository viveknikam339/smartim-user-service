package com.smartim.userservice.service.serviceimpl;

import com.smartim.userservice.dto.AuthResponse;
import com.smartim.userservice.dto.LoginRequest;
import com.smartim.userservice.dto.RegisterRequest;
import com.smartim.userservice.entity.User;
import com.smartim.userservice.exception.ResourceNotFoundException;
import com.smartim.userservice.mapper.UserMapper;
import com.smartim.userservice.repository.UserRepository;
import com.smartim.userservice.service.UserService;
import com.smartim.userservice.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repo;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder encoder;
    private final UserMapper mapper;

    @Override
    public String register(RegisterRequest request) {
        List<User> existingUser = repo.findByEmailOrMobileNumber(request.getEmail(), request.getMobileNumber());
        if(!existingUser.isEmpty()){
            throw new UsernameNotFoundException(
                    "User already registered with given email or mobile number."
            );
        }
        User user = mapper.toUserEntity(request);
        user.setCreatedOn(LocalDateTime.now());
        user.setCreatedBy(user.getUserName());
        repo.save(user);
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        return token;
    }

    @Override
    public String login(LoginRequest request) {
        User user = repo.findByUserName(request.getEmail()).orElseThrow(
                () -> new UsernameNotFoundException("User not found.")
        );
        if (!encoder.matches(request.getPassword(), user.getPassword()))
            throw new BadCredentialsException("Invalid password");
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        return token;
    }

    @Override
    public User getUserByEmail(String email) {
        return repo.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User", "email", email)
        );
    }

    @Override
    public List<User> getUsersByRole(String role) {
        return repo.findByRole(role).orElseThrow(
                () -> new ResourceNotFoundException("User", "role", role)
        );
    }
}
