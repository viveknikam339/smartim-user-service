package com.smartim.userservice.controller;

import com.smartim.userservice.contants.UserConstants;
import com.smartim.userservice.dto.AuthResponse;
import com.smartim.userservice.dto.LoginRequest;
import com.smartim.userservice.dto.RegisterRequest;
import com.smartim.userservice.entity.User;
import com.smartim.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/users", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponse
                        (UserConstants.STATUS_201, UserConstants.MESSAGE_201
                                , userService.register(request)));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponse
                        (UserConstants.STATUS_200, UserConstants.MESSAGE_200
                                , userService.login(request)));
    }

    @GetMapping("/me")
    public ResponseEntity<User> profile(Principal principal){
        return ResponseEntity.ok(userService.getUserByEmail(principal.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id){
        return ResponseEntity.ok(userService.getUserByEmail(id));
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable String role){
        return ResponseEntity.ok(userService.getUsersByRole(role));
    }


}
