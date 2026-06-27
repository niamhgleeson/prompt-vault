package com.example.promptvault.service;

import com.example.promptvault.model.User;
import com.example.promptvault.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    private BCryptPasswordEncoder
            encoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(User user) {
        //adds defaults to the registered user
        user.setRole("USER");

        user.setEnabled(true);

        user.setPassword(encoder.encode(user.getPassword()));

        return userRepository.save(user);

    }

    public User login(
            String username,
            String password
    ) {

        User user = userRepository
                        .findByUsername(username)
                        .orElseThrow();
        if(!user.isEnabled()) {
            throw new RuntimeException("Your account has been disabled. Please contact an administrator");
        }

        boolean matches =
                encoder.matches(
                        password,
                        user.getPassword()
                );

        if (!matches) {
            throw new RuntimeException("Invalid password");

        }

        return user;

    }

    public User findById(Long id) {

        return userRepository
                .findById(id)
                .orElseThrow();

    }
    public List<User>
    getAll() {
        return userRepository.findAll();

    }

    public User setEnabled(
            Long adminId,
            Long userId,
            boolean enabled
    ) {

        User admin =
                findById(adminId);

        if (!"ADMIN".equals(admin.getRole())) {
            throw new RuntimeException(
                    "Admin only"
            );
        }

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