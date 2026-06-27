package com.example.promptvault.dto;

public class FlaggedPromptResponse {

    private String title;
    private String owner;
    private String category;
    private String response;
    private String date;


    public FlaggedPromptResponse(

            String title,
            String owner,
            String category,
            String response,
            String date

    ) {

        this.title = title;
        this.owner = owner;
        this.category = category;
        this.response = response;
        this.date = date;

    }

    public String getTitle() {
        return title;
    }

    public String getOwner() {
        return owner;
    }

    public String getCategory() {
        return category;
    }

    public String getResponse() {
        return response;
    }

    public String getDate() {
        return date;
    }

}
