package com.example.promptvault.controller;

import com.example.promptvault.model.PromptCategory;
import com.example.promptvault.model.User;
import com.example.promptvault.service.UserService;
import com.example.promptvault.service.PromptCategoryService;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class PromptCategoryController {

    private PromptCategoryService promptCategoryService;
    private UserService userService;

    public PromptCategoryController(
            PromptCategoryService promptCategoryService,
            UserService userService
    ) {
        this.promptCategoryService = promptCategoryService;
        this.userService = userService;
    }

    @PostMapping
    public PromptCategory create(
            @Valid
            @RequestBody
            PromptCategory category,

            @RequestParam
            Long userId

    ) {

        User admin =
                userService
                        .findById(userId);

        return promptCategoryService.create(
                category,
                admin
        );

    }
    @GetMapping
    public List<PromptCategory>
    getAll() {
        return promptCategoryService.getAll();
    }

}
