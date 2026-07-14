package com.smartim.userservice.mapper;

import com.smartim.userservice.dto.RegisterRequest;
import com.smartim.userservice.dto.UpdateUserRequest;
import com.smartim.userservice.dto.UserDto;
import com.smartim.userservice.entity.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void toUserEntity_fromRegisterRequest() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("testuser@abc.com");
        registerRequest.setPassword("password");
        registerRequest.setFullName("Test User");
        registerRequest.setMobileNumber("9876543210");
        registerRequest.setRole("ADMIN");

        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        User user = userMapper.toUserEntity(registerRequest, passwordEncoder);

        assertNotNull(user);
        assertEquals("testuser@abc.com", user.getEmail());
        assertEquals("testuser@abc.com", user.getUserName());
        assertEquals("encodedPassword", user.getPassword());
        assertEquals("Test User", user.getFullName());
        assertEquals("9876543210", user.getMobileNumber());
        assertEquals("ADMIN", user.getRole());
        assertTrue(user.getUserStatus());
    }

    @Test
    void toUserEntity_fromUpdateUserRequest() {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setFullName("Updated Name");

        User user = new User();
        user.setFullName("Original Name");

        userMapper.toUserEntity(updateUserRequest, user);

        assertEquals("Updated Name", user.getFullName());
    }

    @Test
    void toUserDtoFromUser() {
        User user = new User();
        user.setId(1L);
        user.setUserName("testuser@abc.com");
        user.setEmail("testuser@abc.com");
        user.setMobileNumber("9876543210");
        user.setFullName("Test User");

        UserDto userDto = userMapper.toUserDtoFromUser(user);

        assertNotNull(userDto);
        assertEquals("testuser@abc.com", userDto.getEmail());
        assertEquals("testuser@abc.com", userDto.getUserName());
        assertEquals("9876543210", userDto.getMobileNumber());
        assertEquals("Test User", userDto.getFullName());
    }



    @Test
    void toUserDtoListFromUserList() {
        User user = new User();
        user.setId(1L);
        user.setUserName("testuser@abc.com");
        user.setEmail("testuser@abc.com");
        user.setMobileNumber("9876543210");
        user.setFullName("Test User");

        List<User> users = Collections.singletonList(user);
        List<UserDto> userDtos = userMapper.toUserDtoListFromUserList(users);

        assertNotNull(userDtos);
        assertEquals(1, userDtos.size());
        assertEquals("testuser@abc.com", userDtos.getFirst().getEmail());
        assertEquals("testuser@abc.com", userDtos.getFirst().getUserName());
        assertEquals("9876543210", userDtos.getFirst().getMobileNumber());
        assertEquals("Test User", userDtos.getFirst().getFullName());
    }
}