package com.smartim.userservice.service.serviceimpl;

import com.smartim.userservice.contants.UserConstants;
import com.smartim.userservice.dto.*;
import com.smartim.userservice.entity.User;
import com.smartim.userservice.exception.ResourceNotFoundException;
import com.smartim.userservice.exception.UserAlreadyExistsException;
import com.smartim.userservice.mapper.UserMapper;
import com.smartim.userservice.repository.UserRepository;
import com.smartim.userservice.service.UserService;
import com.smartim.userservice.util.JwtUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Registers a new user after checking for existing users with the same email or mobile number.
     *
     * @param request contains user details for registration.
     * @return JWT token after successful registration.
     * @throws UsernameNotFoundException if user already exists.
     */
    @Override
    public String register(RegisterRequest request) {
        List<User> existingUser = repo.findByEmailOrMobileNumberOrUserName
                (request.getEmail(), request.getMobileNumber(), request.getUserName());
        if(!existingUser.isEmpty()){
            throw new UserAlreadyExistsException(
                    "User already registered with given email, mobile number or user-name."
            );
        }
        User user = mapper.toUserEntity(request, encoder);
        repo.save(user);
        return jwtUtil.generateToken(user.getUserName(), user.getRole(), user.getEmail());
    }

    /**
     * Update user details after checking for existing users with given user-name and status should be active i.e. true.
     *
     * @param userName contains user username of the user.
     * @param request contains user details for update.
     * @return {@link UserDto} representing the user.
     * @throws UsernameNotFoundException if user-name does not exist or status is inactive i.e. false .
     */
    @Override
    public UserDto updateUserProfile(String userName, UpdateUserRequest request) {
        User user = repo.findByUserNameAndUserStatus(userName, true).orElseThrow(
                () -> new UsernameNotFoundException(UserConstants.USER_NOT_FOUND)
        );
        mapper.toUserEntity(request, user, userName, LocalDateTime.now());
        return mapper.toUserDtoFromUser(repo.save(user));
    }

    /**
     * Update user status ACTIVE/INACTIVE i.e true/false.
     *
     * @param userName contains user username of the user.
     * @return {@link UserDto} representing the user.
     * @throws UsernameNotFoundException if user-name does not exist or status is inactive i.e. false .
     */
    @Override
    public UserDto updateUserStatus(String userName) {
        User user = repo.findByUserName(userName).orElseThrow(
                () -> new UsernameNotFoundException(UserConstants.USER_NOT_FOUND)
        );
        user.setUserStatus(user.getUserStatus() != true);
        return mapper.toUserDtoFromUser(repo.save(user));
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
                () -> new UsernameNotFoundException(UserConstants.USER_NOT_FOUND)
        );
        if (!encoder.matches(request.getPassword(), user.getPassword()))
            throw new BadCredentialsException(UserConstants.ENTERED_WRONG_PASSWORD);
        return jwtUtil.generateToken(user.getUserName(), user.getRole(), user.getEmail());
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

    /**
     * Loads the user by their username and maps it to Spring Security's {@link UserDetails} object.
     *
     * @param userName the username to search for (mapped to userName field)
     * @return a UserDetails object containing username, password, and authorities
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    public UserDto getUserByUserName(String userName) {
        User user = repo.findByUserName(userName).orElseThrow(
                () -> new UsernameNotFoundException(UserConstants.USER_NOT_FOUND)
        );
        return mapper.toUserDtoFromUser(user);
    }

    /**
     * Retrieves users based on optional filters such as email, role, or status.
     *
     * @param email  Optional email filter
     * @param role   Optional role filter
     * @param status Optional status filter
     * @return List of matching users
     */
    @Override
    public List<UserDto> getUsersWithFilters(String email, String role, Boolean status) {
        Specification<User> spec = (root, query, cb) -> cb.conjunction();

        if (email != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("email"), email));
        }

        if (role != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("role"), role));
        }

        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("userStatus"), status));
        }

        return mapper.toUserDtoListFromUserList(repo.findAll(spec));
    }

    /**
     * Updates the role of a user by user-name.
     *
     * @param userName user-name of the user
     * @param role New role
     */
    @Override
    public void updateRole(String userName, String role, String updatedBy) {
        User user = repo.findByUserName(userName).orElseThrow(
                () -> new UsernameNotFoundException(UserConstants.USER_NOT_FOUND)
        );
        user.setRole(role);
        user.setUpdatedOn(LocalDateTime.now());
        user.setUpdatedBy(updatedBy);
        repo.save(user);
    }


    /**
     * Deletes a user by user-name (hard delete).
     *
     * @param userName user-name of the user
     */
    @Transactional
    @Override
    public void deleteUser(String userName) {
        User user = repo.findByUserName(userName).orElseThrow(
                () -> new UsernameNotFoundException(UserConstants.USER_NOT_FOUND)
        );
        entityManager.remove(user);
    }

    public String resetUserPassword(ResetPasswordRequest resetPasswordRequest){
        User user = repo.findByUserName(resetPasswordRequest.getUserName()).orElseThrow(
                () -> new UsernameNotFoundException(UserConstants.USER_NOT_FOUND)
        );
        if (resetPasswordRequest.getPasswordResetType().equals(UserConstants.RESET_PASSWORD) &&
                !encoder.matches(resetPasswordRequest.getOldPassword(), user.getPassword()))
            throw new BadCredentialsException(UserConstants.ENTERED_WRONG_PASSWORD);
        user.setPassword(encoder.encode(resetPasswordRequest.getNewPassword()));
        repo.save(user);
        return UserConstants.PASSWORD_RESET_SUCCESSFULLY;
    }

}
