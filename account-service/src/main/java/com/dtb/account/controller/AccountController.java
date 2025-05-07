package com.dtb.account.controller;

import com.dtb.account.model.dto.AccountBalanceDto;
import com.dtb.account.model.dto.AccountDto;
import com.dtb.account.model.dto.request.CreateAccountRequestDto;
import com.dtb.account.model.dto.request.UpdateAccountRequestDto;
import com.dtb.account.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Account Controller", description = "Api for managing bank accounts")
@RequiredArgsConstructor
@RestController
@RequestMapping("/account/api")
public class AccountController {

    private final AccountService accountService;

    @Operation(
            summary = "Create Account",
            description = "Allows admins to create account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account created"),
            @ApiResponse(responseCode = "401", description = "Invalid Credentials")
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/v1")
    public ResponseEntity<AccountDto> createAccount(@RequestBody @Valid CreateAccountRequestDto request,
                                                    @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        var account = accountService.createAccount(request, token);
        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update Account",
            description = "Partial/complete account details update")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account updated"),
            @ApiResponse(responseCode = "401", description = "Invalid Credentials")
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/v1/{id}")
    public ResponseEntity<AccountDto> updateAccount(@RequestBody @Valid UpdateAccountRequestDto request,
                                                    @PathVariable Long id,
                                                    @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        var account = accountService.updateAccount(request, id, token);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @Operation(
            summary = "Get Account Balance",
            description = "Fetch Account Balance")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Balance fetched"),
            @ApiResponse(responseCode = "401", description = "Invalid Credentials")
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/v1/{id}/balance")
    public ResponseEntity<AccountBalanceDto> getBalance(@PathVariable Long id,
                                                        @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        var accountBalance = accountService.getBalance(id);
        return new ResponseEntity<>(accountBalance, HttpStatus.OK);
    }

    @Operation(
            summary = "Activate Account",
            description = "Activate Account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account activated"),
            @ApiResponse(responseCode = "401", description = "Invalid Credentials")
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/v1/{id}/activate")
    public ResponseEntity<AccountDto> activateAccount(@PathVariable Long id) {
        var accountDto = accountService.activateAccount(id);
        return new ResponseEntity<>(accountDto, HttpStatus.OK);
    }

    @Operation(
            summary = "Deactivate Account",
            description = "Deactivate Account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account deactivated"),
            @ApiResponse(responseCode = "401", description = "Invalid Credentials")
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/v1/{id}/deactivate")
    public ResponseEntity<AccountDto> deactivateAccount(@PathVariable Long id,
                                                        @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        var accountDto = accountService.deactivateAccount(id);
        return new ResponseEntity<>(accountDto, HttpStatus.OK);
    }

}