package com.smartim.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;


import java.time.LocalDateTime;

/**
 * ErrorResponseDto DTO used to return error response
 */
@Data @AllArgsConstructor
public class ErrorResponseDto {

    private String apiPath;

    private HttpStatus errorCode;

    private String errorMessage;

    private LocalDateTime errorTime;

}
