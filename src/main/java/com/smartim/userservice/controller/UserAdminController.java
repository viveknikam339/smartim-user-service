package com.smartim.userservice.controller;

import com.smartim.userservice.dto.ErrorResponseDto;
import com.smartim.userservice.dto.UserDto;
import com.smartim.userservice.entity.User;
import com.smartim.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Tag(name = "Admin APIs", description = "Operations for managing users by admin")
@RequestMapping(value = "/api/admin/users", produces = {MediaType.APPLICATION_JSON_VALUE})
@RestController
@RequiredArgsConstructor
public class UserAdminController {

    private final UserService userService;

    /**
     * Retrieve a list of all users with optional filters.
     *
     * @param email Optional email filter
     * @param role Optional role filter
     * @param status Optional user status filter
     * @return List of users matching the filters
     */
    @Operation(summary = "List all users", description = "Fetch all users with optional filters by email, role, and status")
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
    @GetMapping("/getUsers")
    public ResponseEntity<List<UserDto>> getAllUsers(
            @Parameter(description = "Filter by email") @RequestParam(required = false) String email,
            @Parameter(description = "Filter by role") @RequestParam(required = false) String role,
            @Parameter(description = "Filter by status") @RequestParam(required = false) Boolean status) {

        List<UserDto> users = userService.getUsersWithFilters(email, role, status);
        return ResponseEntity.ok(users);
    }

    /**
     * Update the roles assigned to a specific user.
     *
     * @param userName    user-name of the user to update
     * @param role  New role to assign
     * @return Success message
     */
    @Operation(summary = "Change user role", description = "Assign or update role of a user by user-name")
    @ApiResponse(responseCode = "200", description = "Role updated successfully")
    @PatchMapping("/{userName}/role")
    public ResponseEntity<String> updateUserRole(
            @Parameter(description = "ID of the user to update") @PathVariable String userName,
            @Parameter(description = "New role") @RequestBody String role,
            @Parameter(description = "Logged-in user") Principal principal) {

        userService.updateRole(userName, role, principal.getName());
        return ResponseEntity.ok("User role updated");
    }

    /**
     * Hard delete a user by user-name.
     *
     * @param userName user-name of the user to delete
     * @return Success message
     */
    @Operation(summary = "Delete user", description = "Hard delete the user account by user-name")
    @ApiResponse(responseCode = "200", description = "User deleted successfully")
    @DeleteMapping("/{userName}")
    public ResponseEntity<String> deleteUser(
            @Parameter(description = "ID of the user to delete") @PathVariable String userName) {

        userService.deleteUser(userName);
        return ResponseEntity.ok("User deleted successfully");
    }

    /**
     * Retrieves a list of users by their role.
     *
     * @param role the role to filter users by (e.g., "ADMIN", "USER")
     * @return a response entity containing the list of users with the given role
     */
    @Operation(
            summary = "Get list of User by Role REST API",
            description = "REST API to get list of User by Role inside SMARTIM"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP status OK"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "HTTP status Not Found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserDto>> getUsersByRole(@PathVariable String role) {
        return ResponseEntity.ok(userService.getUsersByRole(role));
    }
}
