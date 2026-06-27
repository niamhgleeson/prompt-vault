package com.example.promptvault.exception;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)

    public Map<String,String>
    handle(RuntimeException e) {

        return Map.of("error", e.getMessage());

    }

}