package com.example.promptvault.controller;

import com.example.promptvault.model.Prompt;
import com.example.promptvault.model.SubmissionHistory;
import com.example.promptvault.model.User;
import com.example.promptvault.service.PromptService;
import com.example.promptvault.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.*;

//handles http requests
@RestController
@RequestMapping("/prompts")
public class PromptController {

    private PromptService promptService;
    private UserService userService;

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
                userService.findByUsername(
                        authentication.getName()
                );

        prompt.setOwner(user);

        return promptService.createPrompt(
                prompt
        );
    }

    @PostMapping("/submit/{id}")
    public SubmissionHistory submit(
            @PathVariable
            Long id,

            Authentication authentication
    ) {

        User user =
                userService.findByUsername(
                        authentication.getName()
                );

        return promptService.submitPrompt(
                id,
                user.getId()
        );

    }

    @GetMapping("/mine")
    public List<Prompt> userPrompts(
            Authentication authentication
    ) {

        User user =
                userService.findByUsername(
                        authentication.getName()
                );

        return promptService.getUserPrompts(
                user.getId()
        );
    }

    @GetMapping("/shared")
    public List<Prompt>
    shared() {

        return promptService.getSharedPrompts();

    }

    @PutMapping("/{id}")
    public Prompt update(
            @PathVariable Long id,
            @Valid @RequestBody Prompt prompt,
            Authentication authentication
    ) {

        User user =
                userService.findByUsername(
                        authentication.getName()
                );

        return promptService.updatePrompt(
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
                userService.findByUsername(
                        authentication.getName()
                );

        promptService.deletePrompt(
                id,
                user.getId()
        );
    }

}
