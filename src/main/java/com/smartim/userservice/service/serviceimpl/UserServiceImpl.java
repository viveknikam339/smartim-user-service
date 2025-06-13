package com.smartim.userservice.service.serviceimpl;

import com.smartim.userservice.dto.AuthResponse;
import com.smartim.userservice.dto.LoginRequest;
import com.smartim.userservice.dto.RegisterRequest;
import com.smartim.userservice.entity.User;
import com.smartim.userservice.repository.UserRepository;
import com.smartim.userservice.service.UserService;
import com.smartim.userservice.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repo;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder encoder;

    @Override
    public AuthResponse register(RegisterRequest request) {
        User user = new User(null, request.getEmail(), encoder.encode(request.getPassword()), request.getFullName(),
                request.getMobileNumber(), true, request.getRole().toUpperCase(), request.getEmail());
        repo.save(user);
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        return new AuthResponse(token);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = repo.findByEmail(request.getEmail()).orElseThrow(
                () -> new UsernameNotFoundException("User not found.")
        );
        if (!encoder.matches(request.getPassword(), user.getPassword()))
            throw new BadCredentialsException("Invalid password");
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        return new AuthResponse(token);
    }

    @Override
    public User getUserByEmail(String email) {
        return repo.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User not found.")
        );
    }

    @Override
    public List<User> getUsersByRole(String role) {
        return repo.findByRole(role).orElseThrow(
                () -> new UsernameNotFoundException("Users not found.")
        );
    }
}
