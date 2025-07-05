package com.smartim.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * ResponseDto DTO used to return success response
 */
@Schema(
        name = "Response",
        description = "Schema to hold response"
)
@Data @AllArgsConstructor
public class ResponseDto {

    @Schema(
            description = "Status code in the response", example = "200"
    )
    private String statusCode;

    @Schema(
            description = "Status message in the response", example = "Request processed successfully"
    )
    private String statusMsg;

}
