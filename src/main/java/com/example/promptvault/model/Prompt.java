package com.example.promptvault.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name="prompts")
public class Prompt {

    @ManyToOne
    private User owner;

    @ManyToOne
    private PromptCategory category;

    @Id
    @GeneratedValue(
            strategy =
                    GenerationType.IDENTITY
    )
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    @Column(length = 3000)
    private String promptText;

    @NotBlank
    private String visibility;

    private boolean flagged;

    public Prompt() {
    }

    public Long getId() {
        return id;
    }

    public void setId(
            Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getPromptText() {
        return promptText;
    }

    public void setPromptText(String promptText) {
        this.promptText = promptText;
    }
    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public PromptCategory getCategory() {
        return category;
    }

    public void setCategory(PromptCategory category) {
        this.category = category;
    }
}
