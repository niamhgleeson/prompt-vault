package com.example.promptvault.repository;

import com.example.promptvault.model.Prompt;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

@Repository
public interface PromptRepository
        extends JpaRepository<Prompt, Long> {

   //List allows multiple prompts
    //findByOwnerId finds all prompts made by one user, useful for owner
    List<Prompt>
    findByOwnerId(Long ownerId);

    List<Prompt>
    findByVisibility(String visibility);

}