package com.example.promptvault.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="submission_history")
public class SubmissionHistory {
    @Id
    @GeneratedValue(
            strategy =
                    GenerationType.IDENTITY
    )
    private Long id;

    private String flaggedKeyword;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="prompt_id")
    private Prompt prompt;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    private LocalDateTime submissionDate;

    @Column(length = 3000)
    private String simulatedResponse;

    private boolean flagged;

    public SubmissionHistory() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Prompt getPrompt() {
        return prompt;
    }

    public void setPrompt(Prompt prompt) {
        this.prompt = prompt;
    }

    public LocalDateTime getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(LocalDateTime submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getSimulatedResponse() {
        return simulatedResponse;
    }

    public void setSimulatedResponse(String simulatedResponse) {
        this.simulatedResponse = simulatedResponse;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    public void setFlaggedKeyword(String flaggedKeyword) {
        this.flaggedKeyword = flaggedKeyword;
    }

    public String getFlaggedKeyword() {
        return flaggedKeyword;
    }

}
