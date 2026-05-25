package com.iexceed.appzillon.appstore.scheduler;

import com.iexceed.appzillon.appstore.entity.ActiveUserEntity;
import com.iexceed.appzillon.appstore.entity.UserSessionEntity;
import com.iexceed.appzillon.appstore.repository.ActiveUserRepository;
import com.iexceed.appzillon.appstore.repository.UserSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

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
        List<UserSessionEntity> expired = sessionRepository.findByStatusAndExpireAtBefore("ACTIVE", now);
        if (expired.isEmpty()) return;
        logger.info("SessionExpiryScheduler found {} expired sessions", expired.size());
        for (UserSessionEntity s : expired) {
            try {
                s.setStatus("EXPIRED");
                sessionRepository.save(s);
                // update active user if exists
                try {
                    java.util.Optional<ActiveUserEntity> a = activeUserRepository.findById(s.getSessionKey());
                    if (a.isPresent()) {
                        ActiveUserEntity au = a.get();
                        au.setStatus("EXPIRED");
                        au.setExpireAt(s.getExpireAt());
                        activeUserRepository.save(au);
                    }
                } catch (Exception ex) {
                    logger.warn("Failed to update active user for session {}: {}", s.getSessionKey(), ex.getMessage());
                }
            } catch (Exception e) {
                logger.error("Failed to expire session {}: {}", s.getSessionKey(), e.getMessage());
            }
        }
    }
}
