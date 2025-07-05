package com.smartim.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * ErrorResponseDto DTO used to get login information
 */
@Schema(
        name = "Login User",
        description = "Schema to hold user information for login"
)
@Data
public class LoginRequest {

    @Schema(
            description = "User name of the user", example = "vnvivek"
    )
    private String userName;

    @Schema(
            description = "Password of the user", example = "Password"
    )
    private String password;

}
