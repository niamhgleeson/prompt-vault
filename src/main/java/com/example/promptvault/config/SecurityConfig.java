package com.example.promptvault.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();

    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/",
                                "/login-page",
                                "/register-page",
                                "/register",
                                "/access-denied",
                                "/css/**"
                        )
                        .permitAll()

                        .requestMatchers(
                                "/admin-dashboard",
                                "/admin-users-page",
                                "/admin-categories-page",
                                "/admin-keywords-page",
                                "/admin-flagged-prompts-page"
                        )
                        .hasRole("ADMIN")

                        .requestMatchers(
                                "/web/users/**",
                                "/web/categories/**",
                                "/web/keywords/**"
                        )
                        .hasRole("ADMIN")

                        .requestMatchers(
                                "/users/**",
                                "/history/flagged"
                        )
                        .hasRole("ADMIN")

                        .anyRequest()
                        .authenticated()

                )

                .formLogin(form -> form

                        .loginPage("/login-page")

                        .loginProcessingUrl("/login")

                        .successHandler(
                                (request,
                                 response,
                                 authentication) -> {

                                    boolean admin =
                                            authentication
                                                    .getAuthorities()
                                                    .stream()
                                                    .anyMatch(
                                                            authority ->
                                                                    authority
                                                                            .getAuthority()
                                                                            .equals("ROLE_ADMIN")
                                                    );

                                    if (admin) {

                                        response.sendRedirect("/admin-dashboard");

                                    } else {

                                        response.sendRedirect("/user-dashboard");

                                    }

                                }
                        )

                        .failureUrl(
                                "/login-page?error=true"
                        )

                        .permitAll()

                )
        .logout(logout -> logout

                .logoutUrl("/logout")

                .logoutSuccessUrl("/login-page?logout=true")

                .invalidateHttpSession(true)

                .deleteCookies("JSESSIONID")

                .permitAll()

        )
                .sessionManagement(session -> session

                        .sessionFixation(
                                fixation ->
                                        fixation.changeSessionId()
                        )

                )

                .exceptionHandling(exception -> exception

                        .accessDeniedPage(
                                "/access-denied"
                        )

                )
                .headers(headers -> headers

                        .contentSecurityPolicy(csp -> csp

                                .policyDirectives(
                                        "default-src 'self'; " +
                                                "script-src 'self'; " +
                                                "style-src 'self'; " +
                                                "img-src 'self' data:; " +
                                                "object-src 'none'; " +
                                                "base-uri 'self'; " +
                                                "frame-ancestors 'none'; " +
                                                "form-action 'self'"
                                )
                        )

                );

        return http.build();

    }
}