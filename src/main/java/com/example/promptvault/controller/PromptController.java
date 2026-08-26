package com.example.promptvault.controller;

import com.example.promptvault.model.Prompt;
import com.example.promptvault.model.SubmissionHistory;
import com.example.promptvault.model.User;

import com.example.promptvault.service.PromptService;
import com.example.promptvault.service.UserService;

import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prompts")
public class PromptController {

    private final PromptService promptService;
    private final UserService userService;

    public PromptController(
            PromptService promptService,
            UserService userService
    ) {

        this.promptService = promptService;
        this.userService = userService;
    }

    @PostMapping
    public Prompt createPrompt(
            @Valid
            @RequestBody Prompt prompt,
            Authentication authentication
    ) {

        User user =
                userService
                        .findByUsername(
                                authentication
                                        .getName()
                        );

        /*
         * Never trust owner information supplied
         * by the client.
         */
        prompt.setOwner(
                user
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
            @RequestBody Prompt prompt,
            Authentication authentication
    ) {

        User user =
                userService
                        .findByUsername(
                                authentication
                                        .getName()
                        );

        return promptService
                .updatePrompt(
                        id,
                        user.getId(),
                        prompt
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