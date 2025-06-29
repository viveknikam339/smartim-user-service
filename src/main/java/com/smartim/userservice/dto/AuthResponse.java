package com.smartim.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {

    private String statusCode;

    private String statusMsg;

    private String token;

}
