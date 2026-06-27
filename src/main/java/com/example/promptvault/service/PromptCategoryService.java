package com.example.promptvault.service;

import com.example.promptvault.model.PromptCategory;
import com.example.promptvault.model.User;
import com.example.promptvault.repository.PromptCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PromptCategoryService {

    private PromptCategoryRepository repository;

    public PromptCategoryService(PromptCategoryRepository repository) {

        this.repository = repository;
    }

    public PromptCategory create(
            PromptCategory category,
            User user
    ) {
        if (!"ADMIN".equals(user.getRole())) {

            throw new RuntimeException(
                    "Only admins can manage categories"
            );

        }

        return repository.save(category);

    }

    public List<PromptCategory>
    getAll() {

        return repository.findAll();
    }
}
