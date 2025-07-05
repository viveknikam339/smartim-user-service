package com.smartim.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * AuthResponse DTO used to return JWT token
 */
@Data
@AllArgsConstructor
public class AuthResponse {

    private String statusCode;

    private String statusMsg;

    private String token;

}
