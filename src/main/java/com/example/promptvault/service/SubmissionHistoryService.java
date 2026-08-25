package com.example.promptvault.service;

import java.time.LocalDateTime;
import java.util.*;

import com.example.promptvault.dto.SubmissionHistoryResponse;
import org.springframework.stereotype.Service;
import com.example.promptvault.model.*;
import com.example.promptvault.repository.SubmissionHistoryRepository;
import com.example.promptvault.dto.FlaggedPromptResponse;

@Service
public class SubmissionHistoryService {
    private SubmissionHistoryRepository repository;

    public SubmissionHistoryService(
            SubmissionHistoryRepository repository
    ) {
        this.repository= repository;
    }

    public SubmissionHistory create(
            Prompt prompt,
            User user,
            String response,
            boolean flagged,
            String flaggedKeyword

    ) {

        SubmissionHistory history = new SubmissionHistory();

        history.setPrompt(prompt);

        history.setSubmissionDate(LocalDateTime.now());

        history.setSimulatedResponse(response);

        history.setFlagged(flagged);

        history.setUser(user);

        history.setFlaggedKeyword(flaggedKeyword);

        return repository.save(history);

    }

    public List<SubmissionHistoryResponse>
    getUserHistory(Long userId) {

        List<SubmissionHistory>
                history =
                repository.findByUserId(userId);

        return history.stream()
                .map(h ->
                        new SubmissionHistoryResponse(
                                h.getPrompt().getTitle(),
                                h.getSimulatedResponse(),
                                h.getSubmissionDate().toString()
                        )

                ).toList();

    }

    public List<SubmissionHistory>
    getFlagged() {

        return repository.findByFlagged(true);

    }

    public List<FlaggedPromptResponse>
    getFlaggedResponses() {

        List<SubmissionHistory>
                history = getFlagged();

        return history
                .stream()
                .map(h ->
                                new FlaggedPromptResponse(
                                        h.getPrompt().getTitle(),
                                        h.getUser().getUsername(),
                                        h.getPrompt().getCategory().getName(),
                                        h.getFlaggedKeyword(),
                                        h.getSubmissionDate().toString()

                                )
                ).toList();

    }
}
