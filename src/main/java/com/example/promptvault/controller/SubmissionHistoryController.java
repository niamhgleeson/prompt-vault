package com.example.promptvault.controller;

import java.util.*;

import com.example.promptvault.dto.FlaggedPromptResponse;
import com.example.promptvault.dto.SubmissionHistoryResponse;
import org.springframework.web.bind.annotation.*;
import com.example.promptvault.model.SubmissionHistory;
import com.example.promptvault.model.User;
import com.example.promptvault.service.SubmissionHistoryService;
import com.example.promptvault.service.UserService;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/history")
public class SubmissionHistoryController {

    private SubmissionHistoryService service;
    private UserService userService;

    public SubmissionHistoryController(

            SubmissionHistoryService service,
            UserService userService

    ) {

        this.service = service;
        this.userService = userService;

    }

    @GetMapping("/mine")
    public List<SubmissionHistoryResponse> getMine(

            Authentication authentication

    ) {

        User user =
                userService.findByUsername(
                        authentication.getName()
                );

        return service.getUserHistory(
                user.getId()
        );

    }

    @GetMapping("/flagged")
    public List<FlaggedPromptResponse>
    flagged() {

        return service.getFlaggedResponses();

    }

}
