package com.example.promptvault.repository;

import com.example.promptvault.model.SubmissionHistory;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

@Repository
public interface SubmissionHistoryRepository
        extends JpaRepository<SubmissionHistory, Long> {

    List<SubmissionHistory>
    findByUserId(Long userId);

    List<SubmissionHistory>
    findByFlagged(boolean flagged);


}