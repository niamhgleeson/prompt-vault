package com.example.promptvault.repository;

import com.example.promptvault.model.PolicyKeyword;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface PolicyKeywordRepository
        extends JpaRepository<PolicyKeyword, Long> {

}