package com.smartim.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * UpdateUserRequest DTO used to get information to update user.
 */
@Schema(
        name = "Update User",
        description = "Schema to hold user information for updation"
)
@Data
public class UpdateUserRequest {

    @Schema(
            description = "Full name of the user", example = "Vivek Nikam"
    )
    private String fullName;

    @Schema(
            description = "Mobile number of the user", example = "1234567890"
    )
    private String mobileNumber;

}
