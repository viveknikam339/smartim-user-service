package com.smartim.userservice.controller;

import com.smartim.userservice.contants.UserConstants;
import com.smartim.userservice.dto.AuthResponse;
import com.smartim.userservice.dto.LoginRequest;
import com.smartim.userservice.dto.RegisterRequest;
import com.smartim.userservice.dto.UserDto;
import com.smartim.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


/**
 * REST controller for handling user-related operations such as registration,
 * login, fetching user profiles and searching users by ID or role.
 */
@RestController
@RequestMapping(value = "/users", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * Registers a new user.
     *
     * @param request the registration details such as name, email, password, etc.
     * @return a response entity containing a JWT token and status message
     */
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
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
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
    @GetMapping("/me")
    public ResponseEntity<UserDto> profile(Principal principal){
        return ResponseEntity.ok(userService.getUserByEmail(principal.getName()));
    }

    /**
     * Retrieves a user by their email (or user ID if used as email).
     *
     * @param id the email or unique identifier of the user
     * @return a response entity containing the user's information
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String id){
        return ResponseEntity.ok(userService.getUserByEmail(id));
    }

    /**
     * Retrieves a list of users by their role.
     *
     * @param role the role to filter users by (e.g., "ADMIN", "USER")
     * @return a response entity containing the list of users with the given role
     */
    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserDto>> getUsersByRole(@PathVariable String role){
        return ResponseEntity.ok(userService.getUsersByRole(role));
    }


}
