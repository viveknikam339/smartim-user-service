package com.smartim.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * AuthResponse DTO used to return JWT token
 */
@Schema(
        name = "Auth Response",
        description = "Schema to hold authentication response"
)
@Data
@AllArgsConstructor
public class AuthResponse {

    @Schema(
            description = "Status code in the response", example = "200"
    )
    private String statusCode;

    @Schema(
            description = "Status message in the response", example = "Request processed successfully"
    )
    private String statusMsg;

    @Schema(
            description = "JWT token in the response", example = "eyJhbGciOiJIUzI1NiJ9....."
    )
    private String token;

}
