package com.dtb.user.profile.controller;

import com.dtb.user.profile.model.dto.CustomerProfileDto;
import com.dtb.user.profile.model.request.CreateProfileRequest;
import com.dtb.user.profile.service.CustomerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Customer Profile Controller", description = "Api to manage customer profile data")
@RequiredArgsConstructor
@RequestMapping("/profile/api")
@RestController
public class CustomerController {

     /*
        Implement a REST API for managing profile data (registration, login).
        Include JWT-based authentication and RBAC.
     */
    private final CustomerService customerService;

    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/v1")
    public ResponseEntity<CustomerProfileDto> createProfile(@RequestBody @Valid CreateProfileRequest request) {
        var customerProfile = customerService.createCustomerProfile(request);
        return new ResponseEntity<>(customerProfile, HttpStatus.CREATED);
    }

    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/v1/{id}")
    public ResponseEntity<CustomerProfileDto> updateProfile(@RequestBody @Valid CreateProfileRequest request,
                                                         @PathVariable String id) {
        var customerProfile = customerService.updateCustomerProfile(request, id);
        return new ResponseEntity<>(customerProfile, HttpStatus.OK);
    }

    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAuthority('OPERATIONS') or hasAuthority('ADMIN')")
    @GetMapping("/v1/{id}")
    public ResponseEntity<CustomerProfileDto> getProfile(@PathVariable String id) {
        var customerProfile = customerService.getCustomerProfile(id);
        return new ResponseEntity<>(customerProfile, HttpStatus.OK);
    }
}