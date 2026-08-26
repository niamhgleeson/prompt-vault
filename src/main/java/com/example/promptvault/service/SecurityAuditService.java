package com.example.promptvault.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SecurityAuditService {

    private static final Logger logger =
            LoggerFactory.getLogger(
                    "SECURITY_AUDIT"
            );

    private String safe(
            String value
    ) {

        if (value == null) {
            return "unknown";
        }

        return value
                .replace('\r', '_')
                .replace('\n', '_')
                .replace('\t', '_');
    }

    public void loginSuccess(
            String username,
            String ipAddress
    ) {

        logger.info(
                "event=LOGIN_SUCCESS username={} ip={}",
                safe(username),
                safe(ipAddress)
        );
    }

    public void loginFailure(
            String username,
            String ipAddress
    ) {

        logger.warn(
                "event=LOGIN_FAILURE username={} ip={}",
                safe(username),
                safe(ipAddress)
        );
    }

    public void loginRateLimited(
            String username,
            String ipAddress
    ) {

        logger.warn(
                "event=LOGIN_RATE_LIMITED username={} ip={}",
                safe(username),
                safe(ipAddress)
        );
    }

    public void unauthorizedPromptAccess(
            Long userId,
            Long promptId
    ) {

        logger.warn(
                "event=UNAUTHORIZED_PROMPT_ACCESS userId={} promptId={}",
                userId,
                promptId
        );
    }

    public void promptFlagged(
            Long userId,
            Long promptId
    ) {

        logger.warn(
                "event=PROMPT_FLAGGED userId={} promptId={}",
                userId,
                promptId
        );
    }

    public void promptRateLimited(
            Long userId
    ) {

        logger.warn(
                "event=PROMPT_RATE_LIMITED userId={}",
                userId
        );
    }

    public void userStatusChanged(
            String adminUsername,
            Long userId,
            boolean enabled
    ) {

        logger.info(
                "event=USER_STATUS_CHANGED admin={} targetUserId={} enabled={}",
                safe(adminUsername),
                userId,
                enabled
        );
    }

    public void categoryChanged(
            String adminUsername,
            Long categoryId,
            String action
    ) {

        logger.info(
                "event=CATEGORY_CHANGE admin={} categoryId={} action={}",
                safe(adminUsername),
                categoryId,
                safe(action)
        );
    }

    public void keywordChanged(
            String adminUsername,
            Long keywordId,
            String action
    ) {

        logger.info(
                "event=KEYWORD_CHANGE admin={} keywordId={} action={}",
                safe(adminUsername),
                keywordId,
                safe(action)
        );
    }
}