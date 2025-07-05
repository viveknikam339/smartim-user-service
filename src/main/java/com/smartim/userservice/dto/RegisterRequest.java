package com.smartim.userservice.dto;

import lombok.Data;

/**
 * ErrorResponseDto DTO used to get information to register user.
 */
@Data
public class RegisterRequest {

    private String email;

    private String password;

    private String fullName;

    private String mobileNumber;

    private String role;

    private String userName;
}
