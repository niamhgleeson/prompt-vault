package com.example.promptvault.config;

import com.example.promptvault.model.User;
import com.example.promptvault.repository.UserRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.password}")
    private String adminPassword;

    private final PasswordEncoder encoder;

    public DataSeeder(
            PasswordEncoder encoder
    ) {

        this.encoder = encoder;

    }

    @Bean
    CommandLineRunner seedData(
            UserRepository userRepository
    ) {

        return args -> {

            if (
                    userRepository
                            .findByUsername(
                                    adminUsername
                            )
                            .isEmpty()
            ) {

                User admin =
                        new User();

                admin.setName(
                        "System"
                );

                admin.setSurname(
                        "Admin"
                );

                admin.setUsername(
                        adminUsername
                );

                admin.setEmail(
                        "admin@promptvault.com"
                );

                admin.setPassword(
                        encoder.encode(
                                adminPassword
                        )
                );

                admin.setRole(
                        "ADMIN"
                );

                admin.setEnabled(
                        true
                );

                userRepository.save(
                        admin
                );

            }

        };

    }

}