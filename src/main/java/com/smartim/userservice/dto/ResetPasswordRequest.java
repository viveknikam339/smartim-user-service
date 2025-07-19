package com.smartim.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * ResetPasswordRequest DTO used to get information to reset user password.
 */
@Schema(
        name = "Reset User Password",
        description = "Schema to hold user information to reset user password"
)
@Data
public class ResetPasswordRequest {

    @Schema(
            description = "User name of the user", example = "vivek1234"
    )
    private String userName;

    @Schema(
            description = "Old password of the user", example = "Password"
    )
    private String oldPassword;

    @Schema(
            description = "New password of the user", example = "Password"
    )
    private String newPassword;

    @Schema(
            description = "Password reset type", example = "FORGOT/RESET"
    )
    private String passwordResetType;

}
