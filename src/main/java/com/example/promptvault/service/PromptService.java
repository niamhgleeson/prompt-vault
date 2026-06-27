package com.example.promptvault.service;

import com.example.promptvault.model.Prompt;
import com.example.promptvault.model.SubmissionHistory;
import com.example.promptvault.model.User;
import com.example.promptvault.repository.PromptRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PromptService {
    private PromptRepository promptRepository;
    private PolicyKeywordService keywordService;
    private SubmissionHistoryService historyService;

    public PromptService(
            PromptRepository promptRepository,
            PolicyKeywordService keywordService,
            SubmissionHistoryService historyService
    ) {

        this.promptRepository = promptRepository;
        this.keywordService = keywordService;
        this.historyService = historyService;
    }

    public Prompt createPrompt(Prompt prompt) {
        prompt.setFlagged(false);

        return promptRepository.save(prompt);
    }

    public List<Prompt>
    getUserPrompts(Long userId) {

        return promptRepository.findByOwnerId(userId);

    }

    public List<Prompt>
    getSharedPrompts() {

        return promptRepository.findByVisibility("SHARED");

    }

    public Prompt updatePrompt(Long promptId, Long userId, Prompt updated) {

        Prompt existing =
                promptRepository.findById(promptId)
                        .orElseThrow();

        if (!existing.getOwner().getId().equals(userId)) {
            throw new RuntimeException("You can only edit your own prompts");
        }

        existing.setTitle(updated.getTitle());

        existing.setPromptText(updated.getPromptText());

        existing.setVisibility(updated.getVisibility());

        existing.setCategory(updated.getCategory());

        return promptRepository.save(existing);

    }

    public void deletePrompt(Long promptId, Long userId) {

        Prompt existing =
                promptRepository.findById(promptId)
                        .orElseThrow();

        if (!existing.getOwner().getId().equals(userId)) {

            throw new RuntimeException("You can only delete your own prompts");

        }

        promptRepository.delete(existing);

    }

    public SubmissionHistory submitPrompt(Long promptId) {
        String response;
        Prompt prompt = promptRepository.findById(promptId).orElseThrow();
        String keyword =
                keywordService.getMatchedKeyword(prompt.getPromptText());
        boolean flagged = keyword != null;

        prompt.setFlagged(flagged);

        promptRepository.save(prompt);

        response = "This is a simulated AI response.";

        if(flagged) {
            // prompt is still flagged and warning can be shown elsewhere
            prompt.setFlagged(true);
        } else {
            prompt.setFlagged(false);
        }
        User owner = Optional.ofNullable(prompt.getOwner())
                        .orElseThrow(
                                () -> new RuntimeException("Prompt has no owner")
                        );
        return historyService.create(
                prompt,
                owner,
                response,
                flagged,
                keyword
        );
    }

    public Prompt findById(Long id) {
        return promptRepository
                .findById(id)
                .orElseThrow();
    }

}
