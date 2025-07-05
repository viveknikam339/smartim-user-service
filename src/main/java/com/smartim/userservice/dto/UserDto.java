package com.smartim.userservice.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * ResponseDto DTO used to return user details
 */
@Data
public class UserDto {

    private String email;

    private String fullName;

    private String mobileNumber;

    private Boolean userStatus;

    private String role;

    private String userName;

    private LocalDateTime createdOn;

    private String createdBy;

    private LocalDateTime updatedOn;

    private String updatedBy;
}
