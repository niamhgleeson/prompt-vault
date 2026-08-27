package com.example.promptvault.service;

import com.example.promptvault.exception.RateLimitExceededException;
import com.example.promptvault.model.Prompt;
import com.example.promptvault.model.SubmissionHistory;
import com.example.promptvault.model.User;
import com.example.promptvault.repository.PromptRepository;
import com.example.promptvault.repository.SubmissionHistoryRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PromptService {

    private final PromptRepository promptRepository;
    private final PolicyKeywordService keywordService;
    private final SubmissionHistoryService historyService;
    private final RateLimitService rateLimitService;
    private final SecurityAuditService securityAuditService;
    private final SubmissionHistoryRepository historyRepository;

    public PromptService(
            PromptRepository promptRepository,
            PolicyKeywordService keywordService,
            SubmissionHistoryService historyService,
            RateLimitService rateLimitService,
            SecurityAuditService securityAuditService,
            SubmissionHistoryRepository historyRepository
    ) {

        this.promptRepository =
                promptRepository;

        this.keywordService =
                keywordService;

        this.historyService =
                historyService;

        this.rateLimitService =
                rateLimitService;

        this.securityAuditService =
                securityAuditService;

        this.historyRepository =
                historyRepository;
    }

    public Prompt createPrompt(
            Prompt prompt
    ) {

        prompt.setFlagged(false);

        return promptRepository.save(
                prompt
        );
    }

    public List<Prompt> getUserPrompts(
            Long userId
    ) {

        return promptRepository
                .findByOwnerId(
                        userId
                );
    }

    public List<Prompt> getSharedPrompts() {

        return promptRepository
                .findByVisibility(
                        "SHARED"
                );
    }

    public Prompt updatePrompt(
            Long promptId,
            Long userId,
            Prompt updated
    ) {

        Prompt existing =
                promptRepository
                        .findById(
                                promptId
                        )
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "Prompt not found."
                                        )
                        );

        if (
                existing.getOwner() == null ||
                        !existing
                                .getOwner()
                                .getId()
                                .equals(userId)
        ) {

            securityAuditService
                    .unauthorizedPromptAccess(
                            userId,
                            promptId
                    );

            throw new RuntimeException(
                    "You can only edit your own prompts."
            );
        }

        existing.setTitle(
                updated.getTitle()
        );

        existing.setPromptText(
                updated.getPromptText()
        );

        existing.setVisibility(
                updated.getVisibility()
        );

        existing.setCategory(
                updated.getCategory()
        );

        return promptRepository.save(
                existing
        );
    }

    @Transactional
    public void deletePrompt(
            Long promptId,
            Long userId
    ) {

        Prompt existing =
                promptRepository
                        .findById(
                                promptId
                        )
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "Prompt not found."
                                        )
                        );

        if (
                existing.getOwner() == null ||
                        !existing
                                .getOwner()
                                .getId()
                                .equals(userId)
        ) {

            securityAuditService
                    .unauthorizedPromptAccess(
                            userId,
                            promptId
                    );

            throw new RuntimeException(
                    "You can only delete your own prompts."
            );
        }

        /*
         * Delete dependent submission history first
         * so the foreign key constraint does not
         * prevent deletion of the prompt.
         */
        historyRepository
                .deleteByPromptId(
                        promptId
                );

        promptRepository.delete(
                existing
        );
    }

    public SubmissionHistory submitPrompt(
            Long promptId,
            Long userId
    ) {

        Prompt prompt =
                promptRepository
                        .findById(
                                promptId
                        )
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "Prompt not found."
                                        )
                        );

        /*
         * Check ownership before doing anything
         * with the prompt.
         */
        if (
                prompt.getOwner() == null ||
                        !prompt
                                .getOwner()
                                .getId()
                                .equals(userId)
        ) {

            securityAuditService
                    .unauthorizedPromptAccess(
                            userId,
                            promptId
                    );

            throw new RuntimeException(
                    "You can only submit your own prompts."
            );
        }

        /*
         * Rate limit prompt submissions.
         */
        if (!rateLimitService.allowPromptSubmission(userId)
        ) {

            securityAuditService.promptRateLimited(userId);

            throw new RateLimitExceededException(
                    "Too many prompt submissions. "
                            + "Please wait one minute "
                            + "and try again."
            );
        }

        String keyword =
                keywordService
                        .getMatchedKeyword(
                                prompt.getPromptText()
                        );

        boolean flagged =
                keyword != null;

        /*
         * Record security event without logging
         * the actual prompt text or sensitive keyword.
         */
        if (flagged) {

            securityAuditService
                    .promptFlagged(
                            userId,
                            promptId
                    );
        }

        prompt.setFlagged(
                flagged
        );

        promptRepository.save(
                prompt
        );

        String response =
                "This is a simulated AI response.";

        User owner =
                Optional
                        .ofNullable(
                                prompt.getOwner()
                        )
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "Prompt has no owner."
                                        )
                        );

        return historyService.create(
                prompt,
                owner,
                response,
                flagged,
                keyword
        );
    }

    public Prompt getUserPromptById(
            Long promptId,
            Long userId
    ) {

        Prompt prompt =
                promptRepository
                        .findById(
                                promptId
                        )
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "Prompt not found."
                                        )
                        );

        if (prompt.getOwner() == null ||
                        !prompt
                                .getOwner()
                                .getId()
                                .equals(userId)
        ) {

            securityAuditService
                    .unauthorizedPromptAccess(
                            userId,
                            promptId
                    );

            throw new RuntimeException(
                    "You can only access your own prompts."
            );
        }

        return prompt;
    }

    public Prompt findById(
            Long id
    ) {

        return promptRepository
                .findById(
                        id
                )
                .orElseThrow(
                        () ->
                                new RuntimeException(
                                        "Prompt not found."
                                )
                );
    }
}