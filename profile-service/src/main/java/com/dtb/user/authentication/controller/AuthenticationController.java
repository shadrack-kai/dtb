package com.dtb.user.authentication.controller;

import com.dtb.user.authentication.model.dto.UserDto;
import com.dtb.user.authentication.model.dto.request.UserCredentialsRequest;
import com.dtb.user.authentication.model.dto.request.LoginRequest;
import com.dtb.user.authentication.model.dto.TokenPair;
import com.dtb.user.authentication.service.AuthenticationService;
import com.dtb.user.authentication.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication Controller", description = "Api to authenticate users")
@RequiredArgsConstructor
@RequestMapping("/users/api")
@RestController
public class AuthenticationController {
    /*
        Implement a REST API for managing profile data (registration, login).
        Include JWT-based authentication and RBAC.
     */

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/v1/sign-up")
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserCredentialsRequest request) {
        var user = userService.createUser(request);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Login",
            description = "Allows user to login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login Successful"),
            @ApiResponse(responseCode = "401", description = "Invalid Credentials")
    })
    @PostMapping("/v1/login")
    public ResponseEntity<TokenPair> login(@RequestBody @Valid LoginRequest request) {
        var tokenPair = authenticationService.login(request.getEmail(), request.getPassword());
        return new ResponseEntity<>(tokenPair, HttpStatus.OK);
    }
}
