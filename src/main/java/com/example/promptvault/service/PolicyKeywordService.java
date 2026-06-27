package com.example.promptvault.service;

import java.util.*;

import com.example.promptvault.model.User;
import org.springframework.stereotype.Service;
import com.example.promptvault.model.PolicyKeyword;
import com.example.promptvault.repository.PolicyKeywordRepository;

@Service
public class PolicyKeywordService {

    private PolicyKeywordRepository repository;

    public PolicyKeywordService(
            PolicyKeywordRepository
                    repository

    ) {
        this.repository = repository;
    }

    public PolicyKeyword create(
            PolicyKeyword keyword,
            User user
    ) {

        if (!"ADMIN".equals(user.getRole())
        ) {

            throw new RuntimeException("Admin only");

        }

        return repository.save(keyword);

    }

    public List<PolicyKeyword>
    getAll() {

        return repository.findAll();

    }

    public void delete(Long id) {

        repository.deleteById(id);

    }
    public boolean containsSensitiveContent(String text) {

        List<PolicyKeyword>
                keywords = repository.findAll();

        text = text.toLowerCase();

       for (PolicyKeyword keyword : keywords) {

            if (text.contains(keyword.getKeyword().toLowerCase())) {

                return true;

            }

        }

        return false;

    }

    public String getMatchedKeyword(
            String text
    ) {
        List<PolicyKeyword>
                keywords = repository.findAll();

        text = text.toLowerCase();

        for (PolicyKeyword keyword : keywords) {
            if (text.contains(keyword.getKeyword().toLowerCase())) {
                return keyword.getKeyword();

            }

        }
        return null;

    }

    public PolicyKeyword update(
            Long id,
            String newKeyword,
            User admin
    ) {
        if (!"ADMIN".equals(admin.getRole())) {
            throw new RuntimeException("Admin only");
        }

        PolicyKeyword existing =
                repository.findById(id)
                        .orElseThrow();

        existing.setKeyword(newKeyword);

        return repository.save(existing);
    }

    public PolicyKeyword findById(Long id) {
        return repository.findById(id)
                .orElseThrow();
    }

}