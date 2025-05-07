package com.dtb.common.util.exception;

import com.dtb.common.util.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

@Slf4j
public class CommonExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.warn(ex.getMessage());
        ErrorResponse response = ErrorResponse.builder()
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        log.warn(ex.getMessage());
        ErrorResponse response = ErrorResponse.builder()
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

        @ExceptionHandler(ResourceAccessException.class)
        public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceAccessException ex) {
            log.warn(ex.getMessage());
            ErrorResponse response = ErrorResponse.builder()
                    .message(ex.getMessage())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorResponse> handleHttpClientErrorException(HttpClientErrorException ex) {
        log.error(ex.getMessage());
        ErrorResponse response = ErrorResponse.builder()
                .message(ex.getResponseBodyAsString())
                .build();
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getStatusCode().value()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleAccountNotActiveException(BadRequestException ex) {
        log.warn(ex.getMessage());
        ErrorResponse response = ErrorResponse.builder()
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllOtherException(Exception ex) {
        log.error(ex.getMessage());
        ErrorResponse response = ErrorResponse.builder()
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
