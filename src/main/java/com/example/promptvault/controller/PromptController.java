package com.example.promptvault.controller;

import com.example.promptvault.model.Prompt;
import com.example.promptvault.model.SubmissionHistory;
import com.example.promptvault.service.PromptService;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.*;

//handles http requests
@RestController
@RequestMapping("/prompts")
public class PromptController {

    private PromptService promptService;

    public PromptController(PromptService promptService) {

        this.promptService = promptService;
    }

    @PostMapping
    public Prompt createPrompt(
            @Valid
            @RequestBody
            Prompt prompt) {

        return promptService.createPrompt(prompt);
    }

    @PostMapping("/submit/{id}")
    public SubmissionHistory submit(@PathVariable Long id) {

        return promptService.submitPrompt(id);

    }

    @GetMapping("/user/{id}")
    public List<Prompt>
    userPrompts(@PathVariable Long id) {

        return promptService.getUserPrompts(id);

    }

    @GetMapping("/shared")
    public List<Prompt>
    shared() {

        return promptService.getSharedPrompts();

    }

    @PutMapping("/{id}")
    public Prompt update(

            @PathVariable
            Long id,

            @RequestParam
            Long userId,

            @Valid
            @RequestBody
            Prompt prompt

    ) {

        return promptService.updatePrompt(id, userId, prompt);

    }

    @DeleteMapping("/{id}")
    public void delete(

            @PathVariable
            Long id,

            @RequestParam
            Long userId

    ) {

        promptService.deletePrompt(id, userId);

    }

}
