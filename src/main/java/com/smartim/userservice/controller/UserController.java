package com.smartim.userservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.smartim.userservice.contants.UserConstants;
import com.smartim.userservice.dto.*;
import com.smartim.userservice.service.RedisService;
import com.smartim.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.core.type.TypeReference;
import java.security.Principal;
import java.util.List;


/**
 * REST controller for handling user-related operations such as registration,
 * login, fetching user profiles and searching users by ID or role.
 */
@Tag(name = "User APIs", description = "Endpoints for user management")
@RestController
@RequestMapping(value = "/users", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final RedisService redisService;

    /**
     * Registers a new user.
     *
     * @param request the registration details such as name, email, password, etc.
     * @return a response entity containing a JWT token and status message
     */
    @Operation(
            summary = "Register User REST API",
            description = "REST API to register new User inside SMARTIM"
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP status CREATED"
    )
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponse
                        (UserConstants.STATUS_201, UserConstants.MESSAGE_201
                                , userService.register(request)));
    }

    /**
     * Authenticates a user and generates a JWT token.
     *
     * @param request the login credentials (username/email and password)
     * @return a response entity containing a JWT token and status message
     */
    @Operation(
            summary = "Login User REST API",
            description = "REST API to login User inside SMARTIM"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP status OK"
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new AuthResponse
                        (UserConstants.STATUS_200, UserConstants.MESSAGE_200
                                , userService.login(request)));
    }

    /**
     * Returns the currently authenticated user's profile information.
     *
     * @param principal the security principal of the currently authenticated user
     * @return a response entity containing the user's profile
     */
    @Operation(
            summary = "Get currently authenticated user's profile",
            description = "Returns the profile details of the authenticated user based on the JWT token"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized – JWT token missing or invalid",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    ))
    })
    @GetMapping("/me")
    public ResponseEntity<UserDto> profile(Principal principal){
        return ResponseEntity.ok(userService.getUserByEmail(principal.getName()));
    }

    /**
     * Retrieves a user by their email.
     *
     * @param emailId the email ID of the user
     * @return a response entity containing the user's information
     */
    @Operation(
            summary = "Get User by email ID REST API",
            description = "REST API to get User by email ID inside SMARTIM"
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
    @GetMapping("/{emailId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String emailId) throws JsonProcessingException {
        UserDto userDto =  redisService.get("user_email_" + emailId, UserDto.class);
        if (userDto == null) {
            userDto = userService.getUserByEmail(emailId);
            redisService.set("user_email_" + emailId, userDto, 300L);
        }
        return ResponseEntity.ok(userDto);
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
    public ResponseEntity<List<UserDto>> getUsersByRole(@PathVariable String role) throws JsonProcessingException {
        List<UserDto> userDto =  redisService.get("users_role_" + role, new TypeReference<List<UserDto>>() {});
        if (userDto == null || userDto.isEmpty()) {
            userDto = userService.getUsersByRole(role);
            redisService.set("users_role_" + role, userDto, 300L);
        }
        return ResponseEntity.ok(userDto);
    }


}
