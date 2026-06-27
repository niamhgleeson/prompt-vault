package com.example.promptvault.dto;

public class SubmissionHistoryResponse {

    private String promptTitle;

    private String simulatedResponse;

    private String submissionDate;

    public SubmissionHistoryResponse(
            String promptTitle,
            String simulatedResponse,
            String submissionDate
    ) {

        this.promptTitle =
                promptTitle;

        this.simulatedResponse =
                simulatedResponse;

        this.submissionDate =
                submissionDate;

    }

    public String getPromptTitle() {
        return promptTitle;
    }

    public String getSimulatedResponse() {
        return simulatedResponse;
    }

    public String getSubmissionDate() {
        return submissionDate;
    }

}