package com.example.promptvault.config;

import com.example.promptvault.model.User;
import com.example.promptvault.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DataSeeder {

    private BCryptPasswordEncoder
            encoder =
            new BCryptPasswordEncoder();
    @Bean
    CommandLineRunner seedData(
            UserRepository userRepository
    ) {

        return args -> {

            if (
                    userRepository
                            .findByUsername(
                                    "admin"
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
                        "admin"
                );

                admin.setEmail(
                        "admin@promptvault.com"
                );

                admin.setPassword(
                        encoder.encode(
                                "admin123"
                        )
                );

                admin.setRole(
                        "ADMIN"
                );

                admin.setEnabled(
                        true
                );

                userRepository
                        .save(admin);

            }

        };

    }

}