package com.example.promptvault.service;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

@Service
public class RateLimitService {

    private static final int MAX_LOGIN_FAILURES = 5;

    private static final Duration LOGIN_BLOCK_TIME =
            Duration.ofMinutes(15);

    private static final int MAX_PROMPT_SUBMISSIONS = 10;

    private static final Duration PROMPT_WINDOW =
            Duration.ofMinutes(1);


    private final Map<String, LoginAttempts>
            loginAttempts = new HashMap<>();

    private final Map<Long, Deque<Instant>>
            promptSubmissions = new HashMap<>();


    private static class LoginAttempts {

        private int failures;

        private Instant blockedUntil;
    }


    public String createLoginKey(
            String username,
            String ipAddress
    ) {

        String safeUsername =
                username == null
                        ? "unknown"
                        : username.toLowerCase();

        String safeIp =
                ipAddress == null
                        ? "unknown"
                        : ipAddress;

        return safeUsername
                + "|"
                + safeIp;
    }


    public synchronized void recordLoginFailure(
            String key
    ) {

        LoginAttempts attempts =
                loginAttempts.computeIfAbsent(
                        key,
                        k -> new LoginAttempts()
                );

        attempts.failures++;

        if (
                attempts.failures
                        >= MAX_LOGIN_FAILURES
        ) {

            attempts.blockedUntil =
                    Instant.now()
                            .plus(
                                    LOGIN_BLOCK_TIME
                            );
        }
    }


    public synchronized boolean isLoginBlocked(
            String key
    ) {

        LoginAttempts attempts =
                loginAttempts.get(key);

        if (
                attempts == null
                        ||
                        attempts.blockedUntil == null
        ) {

            return false;
        }

        if (
                Instant.now()
                        .isAfter(
                                attempts.blockedUntil
                        )
        ) {

            loginAttempts.remove(key);

            return false;
        }

        return true;
    }


    public synchronized void clearLoginFailures(
            String key
    ) {

        loginAttempts.remove(key);
    }


    public synchronized boolean allowPromptSubmission(
            Long userId
    ) {

        Instant now =
                Instant.now();

        Instant cutoff =
                now.minus(
                        PROMPT_WINDOW
                );

        Deque<Instant> submissions =
                promptSubmissions
                        .computeIfAbsent(
                                userId,
                                id ->
                                        new ArrayDeque<>()
                        );

        while (
                !submissions.isEmpty()
                        &&
                        submissions
                                .peekFirst()
                                .isBefore(cutoff)
        ) {

            submissions.removeFirst();
        }

        if (
                submissions.size()
                        >= MAX_PROMPT_SUBMISSIONS
        ) {

            return false;
        }

        submissions.addLast(now);

        return true;
    }
}