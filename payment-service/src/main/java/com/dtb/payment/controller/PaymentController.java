package com.dtb.payment.controller;

import com.dtb.common.util.model.MessageResponse;
import com.dtb.payment.model.dto.request.CreatePaymentRequest;
import com.dtb.payment.model.dto.request.CreateTransferRequest;
import com.dtb.payment.service.PaymentService;
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

@Tag(name = "Payment Controller", description = "Api for making payment")
@RequiredArgsConstructor
@RestController
@RequestMapping("/payment/api")
public class PaymentController {

    //Implement a REST API for handling transactions (topups, withdrawal,transfers).
    //Implement business logic to ensure that transactions are atomic and consistent.

    private final PaymentService paymentService;

    @Operation(
            summary = "TopUp Account",
            description = "Allows admins to topup account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Topup successful"),
            @ApiResponse(responseCode = "401", description = "Invalid Credentials"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/v1/topup")
    public ResponseEntity<MessageResponse> accountTopUp(@RequestBody @Valid CreatePaymentRequest request,
                                                        @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        paymentService.processTopUp(request, token);
        var messageResponse = MessageResponse.builder()
                .message("Account top-up successful")
                .build();
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @Operation(
            summary = "Withdrawal",
            description = "Allows admins to withdraw funds from account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Withdrawal successful"),
            @ApiResponse(responseCode = "401", description = "Invalid Credentials"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/v1/withdrawal")
    public ResponseEntity<MessageResponse> accountWithdrawal(@RequestBody @Valid CreatePaymentRequest request,
                                                             @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        paymentService.processWithdrawal(request, token);
        var messageResponse = MessageResponse.builder()
                .message("Withdrawal successful")
                .build();
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @Operation(
            summary = "Funds Transfer",
            description = "Allows admins to transfer funds")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transfer successful"),
            @ApiResponse(responseCode = "401", description = "Invalid Credentials"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/v1/transfer")
    public ResponseEntity<MessageResponse> accountTransfers(@RequestBody @Valid CreateTransferRequest request,
                                                            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        paymentService.processTransfer(request, token);
        var messageResponse = MessageResponse.builder()
                .message("Transfer successful")
                .build();
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }
}
