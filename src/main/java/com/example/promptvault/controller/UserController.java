package com.example.promptvault.controller;

import com.example.promptvault.model.User;
import com.example.promptvault.service.UserService;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User register(
            @Valid
            @RequestBody
            User user
    ) {

        return userService.register(user);

    }

    @GetMapping
    public List<User>
    getAll() {
        return userService.getAll();
    }

    @PutMapping("/{id}/disable")
    public User disable(
            @PathVariable
            Long id,

            @RequestParam
            Long adminId
    ) {

        return userService
                .setEnabled(
                        adminId,
                        id,
                        false
                );

    }

    @PutMapping("/{id}/enable")
    public User enable(
            @PathVariable
            Long id,

            @RequestParam
            Long adminId
    ) {

        return userService
                .setEnabled(
                        adminId,
                        id,
                        true
                );

    }

}