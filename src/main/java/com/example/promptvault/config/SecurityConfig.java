package com.example.promptvault.config;

import com.example.promptvault.service.RateLimitService;
import com.example.promptvault.service.SecurityAuditService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final RateLimitService rateLimitService;
    private final SecurityAuditService securityAuditService;

    public SecurityConfig(
            RateLimitService rateLimitService,
            SecurityAuditService securityAuditService
    ) {

        this.rateLimitService =
                rateLimitService;

        this.securityAuditService =
                securityAuditService;
    }

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
                                "/admin-flagged-prompts-page",
                                "/admin-keywords-edit-page/**",
                                "/edit-category-page/**"
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

                        .loginPage(
                                "/login-page"
                        )

                        .loginProcessingUrl(
                                "/login"
                        )

                        .successHandler(
                                (
                                        request,
                                        response,
                                        authentication
                                ) -> {

                                    String username =
                                            authentication
                                                    .getName();

                                    String ipAddress =
                                            request
                                                    .getRemoteAddr();

                                    String key =
                                            rateLimitService
                                                    .createLoginKey(
                                                            username,
                                                            ipAddress
                                                    );

                                    /*
                                     * If the user has already reached
                                     * the failed-login limit, do not
                                     * allow a correct password to bypass
                                     * the temporary block.
                                     */
                                    if (
                                            rateLimitService
                                                    .isLoginBlocked(
                                                            key
                                                    )
                                    ) {

                                        securityAuditService
                                                .loginRateLimited(
                                                        username,
                                                        ipAddress
                                                );

                                        SecurityContextHolder
                                                .clearContext();

                                        if (
                                                request
                                                        .getSession(false)
                                                        != null
                                        ) {

                                            request
                                                    .getSession(false)
                                                    .invalidate();
                                        }

                                        response.sendRedirect(
                                                "/login-page?rateLimited=true"
                                        );

                                        return;
                                    }

                                    /*
                                     * Successful login clears any
                                     * previous failed attempts.
                                     */
                                    rateLimitService
                                            .clearLoginFailures(
                                                    key
                                            );

                                    securityAuditService
                                            .loginSuccess(
                                                    username,
                                                    ipAddress
                                            );

                                    boolean admin =
                                            authentication
                                                    .getAuthorities()
                                                    .stream()
                                                    .anyMatch(
                                                            authority ->
                                                                    authority
                                                                            .getAuthority()
                                                                            .equals(
                                                                                    "ROLE_ADMIN"
                                                                            )
                                                    );

                                    if (admin) {

                                        response.sendRedirect(
                                                "/admin-dashboard"
                                        );

                                    } else {

                                        response.sendRedirect(
                                                "/user-dashboard"
                                        );
                                    }
                                }
                        )

                        .failureHandler(
                                (
                                        request,
                                        response,
                                        exception
                                ) -> {

                                    String username =
                                            request
                                                    .getParameter(
                                                            "username"
                                                    );

                                    String ipAddress =
                                            request
                                                    .getRemoteAddr();

                                    String key =
                                            rateLimitService
                                                    .createLoginKey(
                                                            username,
                                                            ipAddress
                                                    );

                                    /*
                                     * Record the failed authentication
                                     * attempt.
                                     */
                                    rateLimitService
                                            .recordLoginFailure(
                                                    key
                                            );

                                    securityAuditService
                                            .loginFailure(
                                                    username,
                                                    ipAddress
                                            );

                                    /*
                                     * If the maximum number of failed
                                     * attempts has now been reached,
                                     * show the rate-limit message.
                                     */
                                    if (
                                            rateLimitService
                                                    .isLoginBlocked(
                                                            key
                                                    )
                                    ) {

                                        securityAuditService
                                                .loginRateLimited(
                                                        username,
                                                        ipAddress
                                                );

                                        response.sendRedirect(
                                                "/login-page?rateLimited=true"
                                        );

                                    } else {

                                        response.sendRedirect(
                                                "/login-page?error=true"
                                        );
                                    }
                                }
                        )

                        .permitAll()
                )

                .logout(logout -> logout

                        .logoutUrl(
                                "/logout"
                        )

                        .logoutSuccessUrl(
                                "/login-page?logout=true"
                        )

                        .invalidateHttpSession(
                                true
                        )

                        .deleteCookies(
                                "JSESSIONID"
                        )

                        .permitAll()
                )

                .sessionManagement(session -> session

                        .sessionFixation(
                                fixation ->
                                        fixation
                                                .changeSessionId()
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