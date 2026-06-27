package com.example.promptvault.repository;

import com.example.promptvault.model.PromptCategory;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface PromptCategoryRepository
        extends JpaRepository<PromptCategory, Long> {

}