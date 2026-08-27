package com.example.promptvault.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class PromptRequest {

    @NotBlank(
            message = "Title is required"
    )
    @Size(
            max = 200,
            message = "Title must be 200 characters or fewer"
    )
    private String title;

    @NotBlank(
            message = "Prompt text is required"
    )
    @Size(
            max = 3000,
            message = "Prompt text must be 3000 characters or fewer"
    )
    private String promptText;

    @NotBlank
    @Pattern(
            regexp = "PRIVATE|SHARED|PUBLIC",
            message = "Visibility must be PRIVATE, SHARED or PUBLIC"
    )
    private String visibility;

    @NotNull(
            message = "Category is required"
    )
    private Long categoryId;

    public PromptRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(
            String title
    ) {
        this.title = title;
    }

    public String getPromptText() {
        return promptText;
    }

    public void setPromptText(
            String promptText
    ) {
        this.promptText = promptText;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(
            String visibility
    ) {
        this.visibility = visibility;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(
            Long categoryId
    ) {
        this.categoryId = categoryId;
    }
}