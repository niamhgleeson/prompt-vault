package com.example.promptvault.controller;

import org.springframework.web.bind.annotation.*;
import com.example.promptvault.model.PolicyKeyword;
import com.example.promptvault.service.PolicyKeywordService;
import com.example.promptvault.model.User;
import com.example.promptvault.service.UserService;
import java.util.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/keywords")
public class PolicyKeywordController {

    private PolicyKeywordService service;
    private UserService userService;

    public PolicyKeywordController(
            PolicyKeywordService service,
            UserService userService
    ) {
        this.service = service;
        this.userService = userService;
    }

    @PostMapping
    public PolicyKeyword create(
            @Valid
            @RequestBody
            PolicyKeyword keyword,

            @RequestParam
            Long userId

    ) {

        User admin =
                userService
                        .findById(userId);

        return service.create(

                keyword,
                admin
        );

    }

    @GetMapping
    public List<PolicyKeyword>
    getAll() {
        return service.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable
            Long id
    ) {
        service.delete(id);
    }
}
