package com.dtb.payment.exception;

import com.dtb.common.util.exception.CommonExceptionHandler;
import com.dtb.common.util.exception.ResourceNotFoundException;
import com.dtb.common.util.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends CommonExceptionHandler {

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(InsufficientBalanceException ex) {
        log.warn(ex.getMessage());
        ErrorResponse response = ErrorResponse.builder()
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
