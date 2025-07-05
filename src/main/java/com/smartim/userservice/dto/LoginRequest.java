package com.smartim.userservice.dto;

import lombok.Data;

/**
 * ErrorResponseDto DTO used to get login information
 */
@Data
public class LoginRequest {

    private String userName;

    private String password;

}
