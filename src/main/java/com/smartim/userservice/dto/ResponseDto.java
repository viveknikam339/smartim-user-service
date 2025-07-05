package com.smartim.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * ResponseDto DTO used to return success response
 */
@Data @AllArgsConstructor
public class ResponseDto {

    private String statusCode;

    private String statusMsg;

}
