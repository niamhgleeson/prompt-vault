package com.example.promptvault.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name="policy_keywords")
public class PolicyKeyword {
    @Id
    @GeneratedValue(
            strategy =
                    GenerationType.IDENTITY
    )
    private Long id;

    @NotBlank
    private String keyword;

    public PolicyKeyword() {
    }

    public Long getId() {
        return id;
    }

    public void setId(
            Long id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(
            String keyword) {
        this.keyword = keyword;
    }

}
