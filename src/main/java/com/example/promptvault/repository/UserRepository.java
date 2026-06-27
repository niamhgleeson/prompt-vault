package com.example.promptvault.repository;

import com.example.promptvault.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

@Repository
public interface UserRepository
        extends JpaRepository<User, Long> {

    Optional<User>
    findByUsername(
            String username
    );
}
