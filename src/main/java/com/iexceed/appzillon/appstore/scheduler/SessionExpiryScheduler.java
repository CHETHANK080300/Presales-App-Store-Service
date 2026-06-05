package com.iexceed.appzillon.appstore.scheduler;

import com.iexceed.appzillon.appstore.repository.ActiveUserRepository;
import com.iexceed.appzillon.appstore.repository.UserSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
public class SessionExpiryScheduler {

    private static final Logger logger = LoggerFactory.getLogger(SessionExpiryScheduler.class);

    private final UserSessionRepository sessionRepository;
    private final ActiveUserRepository activeUserRepository;

    public SessionExpiryScheduler(UserSessionRepository sessionRepository, ActiveUserRepository activeUserRepository) {
        this.sessionRepository = sessionRepository;
        this.activeUserRepository = activeUserRepository;
    }

    // run every minute
    @Scheduled(fixedDelayString = "${session.expiry.check.ms:60000}")
    @Transactional
    public void expireSessions() {
        Instant now = Instant.now();
        // Delete sessions where refresh token has expired
        int deletedSessions = sessionRepository.deleteByRefreshExpireAtBefore(now);
        // Delete active users where access token has expired (and refresh not used to update it)
        int deletedActiveUsers = activeUserRepository.deleteByExpireAtBefore(now);

        if (deletedSessions > 0 || deletedActiveUsers > 0) {
            logger.info("SessionExpiryScheduler cleaned up: {} sessions, {} active users", deletedSessions, deletedActiveUsers);
        }
    }
}
