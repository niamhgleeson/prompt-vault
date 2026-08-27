package com.example.promptvault.controller;

import com.example.promptvault.dto.PromptRequest;
import com.example.promptvault.model.Prompt;
import com.example.promptvault.model.SubmissionHistory;
import com.example.promptvault.model.User;

import com.example.promptvault.service.PromptService;
import com.example.promptvault.service.UserService;
import com.example.promptvault.model.PromptCategory;
import com.example.promptvault.service.PromptCategoryService;

import com.example.promptvault.dto.PromptRequest;

import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prompts")
public class PromptController {

    private final PromptService promptService;
    private final UserService userService;
    private final PromptCategoryService promptCategoryService;

    public PromptController(
            PromptService promptService,
            UserService userService,
            PromptCategoryService promptCategoryService
    ) {

        this.promptService =
                promptService;

        this.userService =
                userService;

        this.promptCategoryService =
                promptCategoryService;
    }

    @PostMapping
    public Prompt createPrompt(
            @Valid
            @RequestBody
            PromptRequest request,

            Authentication authentication
    ) {

        User user =
                userService
                        .findByUsername(
                                authentication
                                        .getName()
                        );

        PromptCategory category =
                promptCategoryService
                        .findById(
                                request.getCategoryId()
                        );

        Prompt prompt =
                new Prompt();

        prompt.setTitle(
                request.getTitle()
        );

        prompt.setPromptText(
                request.getPromptText()
        );

        prompt.setVisibility(
                request.getVisibility()
        );

        prompt.setOwner(
                user
        );

        prompt.setCategory(
                category
        );

        return promptService
                .createPrompt(
                        prompt
                );
    }

    @PostMapping("/submit/{id}")
    public SubmissionHistory submit(
            @PathVariable Long id,
            Authentication authentication
    ) {

        User user =
                userService
                        .findByUsername(
                                authentication
                                        .getName()
                        );

        /*
         * RateLimitExceededException is deliberately
         * not caught here.
         *
         * GlobalExceptionHandler will convert it
         * into HTTP 429 Too Many Requests.
         */
        return promptService
                .submitPrompt(
                        id,
                        user.getId()
                );
    }

    @GetMapping("/mine")
    public List<Prompt> userPrompts(
            Authentication authentication
    ) {

        User user =
                userService
                        .findByUsername(
                                authentication
                                        .getName()
                        );

        return promptService
                .getUserPrompts(
                        user.getId()
                );
    }

    @GetMapping("/shared")
    public List<Prompt> shared() {

        return promptService
                .getSharedPrompts();
    }

    @PutMapping("/{id}")
    public Prompt update(
            @PathVariable Long id,

            @Valid
            @RequestBody
            PromptRequest request,

            Authentication authentication
    ) {

        User user =
                userService
                        .findByUsername(
                                authentication
                                        .getName()
                        );

        PromptCategory category =
                promptCategoryService
                        .findById(
                                request.getCategoryId()
                        );

        Prompt updated =
                new Prompt();

        updated.setTitle(
                request.getTitle()
        );

        updated.setPromptText(
                request.getPromptText()
        );

        updated.setVisibility(
                request.getVisibility()
        );

        updated.setCategory(
                category
        );

        return promptService
                .updatePrompt(
                        id,
                        user.getId(),
                        updated
                );
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id,
            Authentication authentication
    ) {

        User user =
                userService
                        .findByUsername(
                                authentication
                                        .getName()
                        );

        promptService
                .deletePrompt(
                        id,
                        user.getId()
                );
    }
}