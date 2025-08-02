package com.smartim.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * UpdateAddressRequest DTO used to get information to update user address.
 */
@Schema(
        name = "Update User Address",
        description = "Schema to hold user address information to update address"
)
@Data
public class UpdateAddressRequest {

    @Schema(
            description = "Address ID", example = "1234"
    )
    private Long id;

    @Schema(
            description = "Receiver name", example = "Vivek Nikam"
    )
    private String receiverName;

    @Schema(
            description = "mobile number of the receiver", example = "1234567890"
    )
    private String mobileNumber;

    @Schema(
            description = "Address label", example = "Home"
    )
    private String label;

    @Schema(
            description = "Address line1", example = "Z-123A"
    )
    private String line1;

    @Schema(
            description = "Address line2", example = "Block-Z"
    )
    private String line2;

    @Schema(
            description = "Address line3", example = "Sector-12"
    )
    private String line3;

    @Schema(
            description = "City", example = "Noida"
    )
    private String city;

    @Schema(
            description = "State or Province", example = "Uttar Pradesh"
    )
    private String state;

    @Schema(
            description = "Postal code", example = "123456"
    )
    private String postalCode;

    @Schema(
            description = "Country", example = "India"
    )
    private String country;

    @Schema(
            description = "Plus code of address", example = "H9XV+88, Noida, Uttar Pradesh"
    )
    private String plusCode;
}
