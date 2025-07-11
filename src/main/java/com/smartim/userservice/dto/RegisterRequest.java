package com.smartim.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * RegisterRequest DTO used to get information to register user.
 */
@Schema(
        name = "Register User",
        description = "Schema to hold user information for registration"
)
@Data
public class RegisterRequest {

    @Schema(
            description = "Email of the user", example = "xyx@xyz.com"
    )
    private String email;

    @Schema(
            description = "Password of the user", example = "Password"
    )
    private String password;

    @Schema(
            description = "Full name of the user", example = "Vivek Nikam"
    )
    private String fullName;

    @Schema(
            description = "Mobile number of the user", example = "1234567890"
    )
    private String mobileNumber;

    @Schema(
            description = "Role of the user", example = "ADMIN"
    )
    private String role;

    @Schema(
            description = "User name of the user", example = "vnvivek"
    )
    private String userName;
}
