package com.smartim.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * ResponseDto DTO used to return user details
 */
@Schema(
        name = "User",
        description = "Schema to hold user information"
)
@Data
public class UserDto {

    @Schema(
            description = "Email of the user", example = "xyx@xyz.com"
    )
    private String email;

    @Schema(
            description = "Full name of the user", example = "Vivek Nikam"
    )
    private String fullName;

    @Schema(
            description = "Mobile number of the user", example = "1234567890"
    )
    private String mobileNumber;

    @Schema(
            description = "User status of the user", example = "true"
    )
    private Boolean userStatus;

    @Schema(
            description = "Role of the user", example = "ADMIN"
    )
    private String role;

    @Schema(
            description = "User name of the user", example = "vnvivek"
    )
    private String userName;

    @Schema(
            description = "Date time on which user got registered", example = "2025-07-05T19:21:33.727138"
    )
    private LocalDateTime createdOn;

    @Schema(
            description = "User name of the person by whom user got registered", example = "vnvivek"
    )
    private String createdBy;

    @Schema(
            description = "Date time on which user got updated", example = "2025-07-05T19:21:33.727138"
    )
    private LocalDateTime updatedOn;

    @Schema(
            description = "User name of the person by whom user got updated", example = "vnvivek"
    )
    private String updatedBy;
}
