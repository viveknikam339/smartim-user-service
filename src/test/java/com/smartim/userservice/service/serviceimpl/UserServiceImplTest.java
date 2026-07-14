package com.smartim.userservice.service.serviceimpl;

import com.smartim.userservice.contants.UserConstants;
import com.smartim.userservice.dto.*;
import com.smartim.userservice.entity.User;
import com.smartim.userservice.exception.ResourceNotFoundException;
import com.smartim.userservice.exception.UserAlreadyExistsException;
import com.smartim.userservice.mapper.UserMapper;
import com.smartim.userservice.repository.UserRepository;
import com.smartim.userservice.util.JwtUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private RegisterRequest registerRequest;
    private UpdateUserRequest updateUserRequest;
    private LoginRequest loginRequest;
    private ResetPasswordRequest resetPasswordRequest;
    private UserDto userDto;

    private static final String USER_NAME = "test@example.com";
    private static final String PASSWORD = "encodedPassword";
    private static final String ROLE = "USER";
    private static final String MOBILE_NUMBER = "1234567890";
    private static final String EMAIL = "test@example.com";


    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUserName(USER_NAME);
        user.setEmail(EMAIL);
        user.setMobileNumber(MOBILE_NUMBER);
        user.setPassword(PASSWORD);
        user.setRole(ROLE);
        user.setUserStatus(true);

        userDto = new UserDto();
        userDto.setUserName(USER_NAME);
        userDto.setEmail(EMAIL);
        userDto.setRole(ROLE);
        userDto.setUserStatus(true);

        registerRequest = new RegisterRequest();
        registerRequest.setEmail(EMAIL);
        registerRequest.setMobileNumber(MOBILE_NUMBER);
        registerRequest.setPassword("password");

        updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setFullName(USER_NAME);

        loginRequest = new LoginRequest();
        loginRequest.setUserName(USER_NAME);
        loginRequest.setPassword("password");

        resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setUserName(USER_NAME);
        resetPasswordRequest.setOldPassword("password");
        resetPasswordRequest.setNewPassword("newPassword");
        resetPasswordRequest.setPasswordResetType(UserConstants.RESET_CRED);
    }

    @Test
    void register_Success() {
        when(userRepository.findByEmailOrMobileNumber(anyString(), anyString())).thenReturn(Collections.emptyList());
        when(userMapper.toUserEntity(registerRequest, passwordEncoder)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(jwtUtil.generateToken(USER_NAME, ROLE, EMAIL)).thenReturn("mockToken");

        String token = userService.register(registerRequest);

        assertNotNull(token);
        assertEquals("mockToken", token);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void register_UserAlreadyExists() {
        when(userRepository.findByEmailOrMobileNumber(anyString(), anyString())).thenReturn(List.of(user));

        assertThrows(UserAlreadyExistsException.class, () -> userService.register(registerRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUserProfile_Success() {
        when(userRepository.findByUserNameAndUserStatus(USER_NAME, true)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserDtoFromUser(user)).thenReturn(userDto);

        UserDto result = userService.updateUserProfile(USER_NAME, updateUserRequest);

        assertNotNull(result);
        assertEquals(USER_NAME, result.getUserName());
        verify(userMapper, times(1)).toUserEntity(updateUserRequest, user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUserProfile_UserNotFound() {
        when(userRepository.findByUserNameAndUserStatus(USER_NAME, true)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.updateUserProfile(USER_NAME, updateUserRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUserStatus_Success() {
        when(userRepository.findByUserName(USER_NAME)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserDtoFromUser(user)).thenReturn(userDto);

        UserDto result = userService.updateUserStatus(USER_NAME);

        assertFalse(user.getUserStatus()); // Verify status toggled
        assertNotNull(result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUserStatus_UserNotFound() {
        when(userRepository.findByUserName(USER_NAME)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.updateUserStatus(USER_NAME));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void login_Success() {
        when(userRepository.findByUserName(USER_NAME)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", PASSWORD)).thenReturn(true);
        when(jwtUtil.generateToken(USER_NAME, ROLE, EMAIL)).thenReturn("mockToken");

        String token = userService.login(loginRequest);

        assertNotNull(token);
        assertEquals("mockToken", token);
    }

    @Test
    void login_UserNotFound() {
        when(userRepository.findByUserName(USER_NAME)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.login(loginRequest));
    }

    @Test
    void login_BadCredentials() {
        when(userRepository.findByUserName(USER_NAME)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", PASSWORD)).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> userService.login(loginRequest));
    }

    @Test
    void getUserByEmail_Success() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(userMapper.toUserDtoFromUser(user)).thenReturn(userDto);

        UserDto result = userService.getUserByEmail(EMAIL);

        assertNotNull(result);
        assertEquals(EMAIL, result.getEmail());
    }

    @Test
    void getUserByEmail_NotFound() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserByEmail(EMAIL));
    }

    @Test
    void getUsersByRole_Success() {
        when(userRepository.findByRole(ROLE)).thenReturn(Optional.of(List.of(user)));
        when(userMapper.toUserDtoListFromUserList(List.of(user))).thenReturn(List.of(userDto));

        List<UserDto> result = userService.getUsersByRole(ROLE);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(ROLE, result.getFirst().getRole());
    }

    @Test
    void getUsersByRole_NotFound() {
        when(userRepository.findByRole(ROLE)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUsersByRole(ROLE));
    }

    @Test
    void getUserByUserName_Success() {
        when(userRepository.findByUserName(USER_NAME)).thenReturn(Optional.of(user));
        when(userMapper.toUserDtoFromUser(user)).thenReturn(userDto);

        UserDto result = userService.getUserByUserName(USER_NAME);

        assertNotNull(result);
        assertEquals(USER_NAME, result.getUserName());
    }

    @Test
    void getUserByUserName_NotFound() {
        when(userRepository.findByUserName(USER_NAME)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.getUserByUserName(USER_NAME));
    }

    @Test
    void getUsersWithFilters() {
        List<User> userList = List.of(user);
        when(userRepository.findAll(any(Specification.class))).thenReturn(userList);
        when(userMapper.toUserDtoListFromUserList(userList)).thenReturn(List.of(userDto));

        List<UserDto> result = userService.getUsersWithFilters(EMAIL, ROLE, true);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void updateRole_Success() {
        when(userRepository.findByUserName(USER_NAME)).thenReturn(Optional.of(user));

        userService.updateRole(USER_NAME, "ADMIN", "adminUser");

        assertEquals("ADMIN", user.getRole());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateRole_NotFound() {
        when(userRepository.findByUserName(USER_NAME)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.updateRole(USER_NAME, "ADMIN", "adminUser"));
    }

    @Test
    void deleteUser_Success() {
        when(userRepository.findByUserName(USER_NAME)).thenReturn(Optional.of(user));

        userService.deleteUser(USER_NAME);

        verify(entityManager, times(1)).remove(user);
    }

    @Test
    void deleteUser_NotFound() {
        when(userRepository.findByUserName(USER_NAME)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.deleteUser(USER_NAME));
    }

    @Test
    void resetUserPassword_Success() {
        when(userRepository.findByUserName(USER_NAME)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", PASSWORD)).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("newEncodedPassword");

        String result = userService.resetUserPassword(resetPasswordRequest);

        assertEquals(UserConstants.PASSWORD_RESET_SUCCESSFULLY, result);
        assertEquals("newEncodedPassword", user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void resetUserPassword_BadCredentials() {
        when(userRepository.findByUserName(USER_NAME)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", PASSWORD)).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> userService.resetUserPassword(resetPasswordRequest));
    }

    @Test
    void resetUserPassword_ForgotPassword_Success() {
        resetPasswordRequest.setPasswordResetType(UserConstants.FORGOT_CRED);
        when(userRepository.findByUserName(USER_NAME)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newPassword")).thenReturn("newEncodedPassword");

        String result = userService.resetUserPassword(resetPasswordRequest);

        assertEquals(UserConstants.PASSWORD_RESET_SUCCESSFULLY, result);
        assertEquals("newEncodedPassword", user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void resetUserPassword_UserNotFound() {
        when(userRepository.findByUserName(USER_NAME)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.resetUserPassword(resetPasswordRequest));
    }
}