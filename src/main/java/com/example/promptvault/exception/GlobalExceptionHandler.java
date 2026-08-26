package com.example.promptvault.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(
            RateLimitExceededException.class
    )
    public ResponseEntity<Map<String, String>>
    handleRateLimit(
            RateLimitExceededException e
    ) {

        return ResponseEntity
                .status(
                        HttpStatus.TOO_MANY_REQUESTS
                )
                .body(
                        Map.of(
                                "error",
                                e.getMessage()
                        )
                );
    }

    @ExceptionHandler(
            RuntimeException.class
    )
    public ResponseEntity<Map<String, String>>
    handleRuntimeException(
            RuntimeException e
    ) {

        return ResponseEntity
                .status(
                        HttpStatus.BAD_REQUEST
                )
                .body(
                        Map.of(
                                "error",
                                e.getMessage()
                        )
                );
    }
}