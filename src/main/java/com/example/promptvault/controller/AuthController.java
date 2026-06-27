package com.example.promptvault.controller;

import com.example.promptvault.dto.LoginRequest;
import com.example.promptvault.model.User;
import com.example.promptvault.service.UserService;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private UserService service;

    public AuthController(UserService service) {

        this.service= service;

    }

    @PostMapping("/login")
    public User login(
            @Valid
            @RequestBody
            LoginRequest request
    ) {

        return service.login(
                request.getUsername(),
                request.getPassword()
        );

    }

    @PostMapping("/logout")
    public String logout() {
        return "Logged out";
    }

}