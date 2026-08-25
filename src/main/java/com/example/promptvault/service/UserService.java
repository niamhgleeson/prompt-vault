package com.example.promptvault.service;

import com.example.promptvault.model.User;
import com.example.promptvault.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder encoder
    ) {

        this.userRepository =
                userRepository;

        this.encoder =
                encoder;

    }

    public User register(User user) {

        user.setRole("USER");
        user.setEnabled(true);
        user.setPassword(
                encoder.encode(user.getPassword())
        );

        return userRepository.save(user);
    }

    public User findById(Long id
    ) {

        return userRepository
                .findById(id)
                .orElseThrow();

    }

    public User findByUsername(String username) {

        return userRepository
                .findByUsername(username)
                .orElseThrow();

    }

    public List<User> getAll() {

        return userRepository.findAll();

    }

    public User setEnabled(
            Long userId,
            boolean enabled
    ) {

        User user =
                findById(userId);

        if ("ADMIN".equals(user.getRole())) {
            throw new RuntimeException(
                    "Admin accounts cannot be disabled."
            );
        }

        user.setEnabled(
                enabled
        );

        return userRepository.save(
                user
        );
    }
}