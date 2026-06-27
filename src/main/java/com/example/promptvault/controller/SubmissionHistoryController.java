package com.example.promptvault.controller;

import java.util.*;

import com.example.promptvault.dto.FlaggedPromptResponse;
import com.example.promptvault.dto.SubmissionHistoryResponse;
import org.springframework.web.bind.annotation.*;
import com.example.promptvault.model.SubmissionHistory;
import com.example.promptvault.service.SubmissionHistoryService;

@RestController
@RequestMapping("/history")
public class SubmissionHistoryController {

    private SubmissionHistoryService service;

    public SubmissionHistoryController(

            SubmissionHistoryService service

    ) {

        this.service = service;

    }

    @GetMapping("/user/{id}")
    public List<SubmissionHistoryResponse> history(

            @PathVariable
            Long id

    ) {

        return service.getUserHistory(id);

    }

    @GetMapping("/flagged")
    public List<FlaggedPromptResponse>
    flagged(
            @RequestParam
            Long adminId
    ) {

        return service.getFlaggedResponses(adminId);

    }

}
