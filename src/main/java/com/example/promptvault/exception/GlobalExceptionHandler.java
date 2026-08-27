package com.example.promptvault.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger =
            LoggerFactory.getLogger(
                    GlobalExceptionHandler.class
            );

    @ExceptionHandler(
            RateLimitExceededException.class
    )
    public ResponseEntity<Map<String, String>>
    handleRateLimit(
            RateLimitExceededException e
    ) {

        /*
         * This message is safe because it is created
         * by our own application.
         */
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

        /*
         * Keep the full technical details in the
         * server-side application log.
         */
        logger.error(
                "Application request failed",
                e
        );

        /*
         * Do not expose database, SQL, Hibernate,
         * file path, stack trace or framework details
         * to the client.
         */
        return ResponseEntity
                .status(
                        HttpStatus.BAD_REQUEST
                )
                .body(
                        Map.of(
                                "error",
                                "The request could not be completed."
                        )
                );
    }
}