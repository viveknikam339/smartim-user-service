package com.smartim.userservice.service.serviceimpl;

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
import com.smartim.userservice.dto.UserDto;
import java.util.List;

/**
 * Service implementation for managing users in the SmartIM application.
 * This class handles user registration, login, and retrieval operations
 * such as fetching user details by email or by role. It also generates JWT tokens
 * upon successful registration and login for authentication purposes.
 * Uses {@link UserRepository} for persistence, {@link JwtUtil} for JWT generation,
 * and {@link PasswordEncoder} for password hashing and verification.
 * Key operations:
 *   User registration with duplicate email/mobile check</li>
 *   User login with credential validation</li>
 *   Get user by email</li>
 *   <Get all users by role</li>
 *
 * @see com.smartim.userservice.service.UserService
 * @see com.smartim.userservice.repository.UserRepository
 * @see com.smartim.userservice.util.JwtUtil
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repo;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder encoder;
    private final UserMapper mapper;

    /**
     * Registers a new user after checking for existing users with the same email or mobile number.
     *
     * @param request contains user details for registration.
     * @return JWT token after successful registration.
     * @throws UsernameNotFoundException if user already exists.
     */
    @Override
    public String register(RegisterRequest request) {
        List<User> existingUser = repo.findByEmailOrMobileNumber(request.getEmail(), request.getMobileNumber());
        if(!existingUser.isEmpty()){
            throw new UsernameNotFoundException(
                    "User already registered with given email or mobile number."
            );
        }
        User user = mapper.toUserEntity(request, encoder);
        repo.save(user);
        return jwtUtil.generateToken(user.getUserName(), user.getRole());
    }

    /**
     * Authenticates user credentials and returns a JWT token.
     *
     * @param request contains username and password.
     * @return JWT token if authentication is successful.
     * @throws UsernameNotFoundException if the user is not found.
     * @throws BadCredentialsException if the password is invalid.
     */
    @Override
    public String login(LoginRequest request) {
        User user = repo.findByUserName(request.getUserName()).orElseThrow(
                () -> new UsernameNotFoundException("User not found.")
        );
        if (!encoder.matches(request.getPassword(), user.getPassword()))
            throw new BadCredentialsException("Invalid password");
        return jwtUtil.generateToken(user.getUserName(), user.getRole());
    }

    /**
     * Retrieves a user by email.
     *
     * @param email the user's email.
     * @return {@link UserDto} representing the user.
     * @throws ResourceNotFoundException if the user is not found.
     */
    @Override
    public UserDto getUserByEmail(String email) {
        User user =  repo.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User", "email", email)
        );
        return mapper.toUserDtoFromUser(user);
    }

    /**
     * Retrieves all users with the specified role.
     *
     * @param role the role to filter users by.
     * @return List of {@link UserDto} objects.
     * @throws ResourceNotFoundException if no users are found with the given role.
     */
    @Override
    public List<UserDto> getUsersByRole(String role) {
        List<User> user =  repo.findByRole(role).orElseThrow(
                () -> new ResourceNotFoundException("User", "role", role)
        );
        return mapper.toUserDtoListFromUserList(user);
    }
}
