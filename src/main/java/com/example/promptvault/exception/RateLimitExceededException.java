package com.example.promptvault.exception;

public class RateLimitExceededException
        extends RuntimeException {

    public RateLimitExceededException(
            String message
    ) {
        super(message);
    }
}