package com.smartim.userservice.controller;

import com.smartim.userservice.dto.AddAddressRequest;
import com.smartim.userservice.dto.AddressDto;
import com.smartim.userservice.dto.UpdateAddressRequest;
import com.smartim.userservice.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller to manage user addresses for the currently authenticated user.
 */
@RestController
@RequestMapping("/api/users/me/addresses")
@RequiredArgsConstructor
@Tag(name = "User Addresses", description = "Manage current user's addresses")
public class UserAddressController {

    private final AddressService addressService;

    /**
     * Retrieves the username of the currently authenticated user.
     *
     * @return username (typically email or username)
     */
    private String getCurrentUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    /**
     * Get a list of all addresses for the logged-in user.
     *
     * @return List of AddressDto
     */
    @Operation(
            summary = "List all addresses",
            description = "Returns a list of all saved addresses for the currently authenticated user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Addresses retrieved",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AddressDto.class)))
            }
    )
    @GetMapping
    public ResponseEntity<List<AddressDto>> getAddresses() {
        String userName = getCurrentUserName();
        return ResponseEntity.ok(addressService.getAddresses(userName));
    }

    /**
     * Add a new address for the logged-in user.
     *
     * @param address AddAddressRequest containing address details
     * @return Saved address as AddressDto
     */
    @Operation(
            summary = "Add a new address",
            description = "Adds a new address for the currently authenticated user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Address added successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AddressDto.class)))
            }
    )
    @PostMapping("/addAddress")
    public ResponseEntity<AddressDto> addAddress(@RequestBody AddAddressRequest address) {
        String userName = getCurrentUserName();
        return ResponseEntity.ok(addressService.addAddress(userName, address));
    }

    /**
     * Update an existing address for the logged-in user.
     *
     * @param address UpdateAddressRequest containing address ID and new values
     * @return Updated address as AddressDto
     */
    @Operation(
            summary = "Update an existing address",
            description = "Updates the given address for the currently authenticated user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Address updated successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AddressDto.class)))
            }
    )
    public ResponseEntity<AddressDto> updateAddress(
            @RequestBody UpdateAddressRequest address) {
        String userName = getCurrentUserName();
        return ResponseEntity.ok(addressService.updateAddress(userName, address));
    }

    /**
     * Delete one or more addresses for the logged-in user.
     *
     * @param addressIds List of address IDs to delete
     * @return HTTP 200 on successful deletion
     */
    @Operation(
            summary = "Delete addresses",
            description = "Deletes the selected address(es) of the currently authenticated user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Addresses deleted successfully")
            }
    )
    @DeleteMapping("/deleteAddresses")
    public ResponseEntity<Void> deleteAddress(@RequestBody List<Long> addressIds) {
        addressService.deleteAddresses(addressIds);
        return ResponseEntity.ok().build();
    }
}

