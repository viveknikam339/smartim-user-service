package com.smartim.userservice.dto;

import lombok.Data;

@Data
public class RegisterRequest {

    private String email;

    private String password;

    private String fullName;

    private String mobileNumber;
}
