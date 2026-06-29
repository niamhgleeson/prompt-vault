package com.example.promptvault.service;

import com.example.promptvault.model.PromptCategory;
import com.example.promptvault.model.User;
import com.example.promptvault.repository.PromptCategoryRepository;
import com.example.promptvault.repository.PromptRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PromptCategoryService {

    private PromptCategoryRepository repository;
    private PromptRepository promptRepository;

    public PromptCategoryService(
            PromptCategoryRepository repository,
            PromptRepository promptRepository

    ) {

        this.repository = repository;
        this.promptRepository = promptRepository;

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

    public void delete(Long id) {

        if (promptRepository.existsByCategoryId(id)) {
            throw new RuntimeException(
                    "Cannot delete this category as it is currently in use."
            );
        }

        repository.deleteById(id);
    }

    public PromptCategory findById(Long id) {

        return repository
                .findById(id)
                .orElseThrow();

    }

    public PromptCategory update(
            Long id,
            PromptCategory updated
    ) {

        PromptCategory existing =
                repository
                        .findById(id)
                        .orElseThrow();

        existing.setName(
                updated.getName()
        );

        existing.setDescription(
                updated.getDescription()
        );

        return repository.save(existing);

    }

    public List<PromptCategory>
    getAll() {

        return repository.findAll();
    }
}
