package com.smartim.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;


import java.time.LocalDateTime;

/**
 * ErrorResponseDto DTO used to return error response
 */
@Schema(
        name = "Error Response",
        description = "Schema to hold error response"
)
@Data @AllArgsConstructor
public class ErrorResponseDto {

    @Schema(
            description = "Api path in the response", example = "/users/login"
    )
    private String apiPath;

    @Schema(
            description = "Error code in the response", example = "400"
    )
    private HttpStatus errorCode;

    @Schema(
            description = "Error message in the response", example = "Request processed successfully"
    )
    private String errorMessage;

    @Schema(
            description = "Date time on which error occured", example = "2025-07-05T19:21:33.727138"
    )
    private LocalDateTime errorTime;

}
