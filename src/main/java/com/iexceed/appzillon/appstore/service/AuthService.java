package com.iexceed.appzillon.appstore.service;

import com.iexceed.appzillon.appstore.dto.AuthResponse;
import com.iexceed.appzillon.appstore.dto.LoginRequest;
import com.iexceed.appzillon.appstore.entity.ActiveUserEntity;
import com.iexceed.appzillon.appstore.entity.UserEntity;
import com.iexceed.appzillon.appstore.entity.UserSessionEntity;
import com.iexceed.appzillon.appstore.repository.ActiveUserRepository;
import com.iexceed.appzillon.appstore.repository.UserRepository;
import com.iexceed.appzillon.appstore.repository.UserSessionRepository;
import com.iexceed.appzillon.appstore.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final UserSessionRepository sessionRepository;
    private final JwtUtil jwtUtil;
    private final ActiveUserRepository activeUserRepository;

    @Value("${auth.max.failed.attempts}")
    private int maxFailedAttempts;

    @Value("${jwt.expiration}")
    private long jwtExpiration;
    
    @Value("${jwt.refreshExpiration}")
    private long refreshExpiration;

    public AuthService(UserRepository userRepository, UserSessionRepository sessionRepository, JwtUtil jwtUtil, ActiveUserRepository activeUserRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.jwtUtil = jwtUtil;
        this.activeUserRepository = activeUserRepository;
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        if (request == null || request.getUsername() == null || request.getPassword() == null || request.getUsername().isBlank() || request.getPassword().isBlank()) {
            return AuthResponse.error("Invalid input");
        }

        Optional<UserEntity> userOpt = userRepository.findByUsername(request.getUsername());
        if (userOpt.isEmpty()) {
            logger.warn("Invalid username: {}", request.getUsername());
            return AuthResponse.error("User does not exist");
        }

        UserEntity user = userOpt.get();
        if ("Y".equalsIgnoreCase(user.getUserLocked())) {
            logger.warn("Locked account attempt: {}", request.getUsername());
            return AuthResponse.error("User account locked. Contact administrator.");
        }

        // new checks: user_status and authorised_status
        if (user.getUserStatus() != null && "BLOCKED".equalsIgnoreCase(user.getUserStatus())) {
            logger.warn("Blocked account attempt: {}", request.getUsername());
            return AuthResponse.error("User account blocked. Contact administrator.");
        }
        if (user.getAuthorisedStatus() != null && "U".equalsIgnoreCase(user.getAuthorisedStatus())) {
            logger.warn("Unauthorised account attempt: {}", request.getUsername());
            return AuthResponse.error("User not authorised to login. Contact administrator.");
        }

        String hashed = sha256Hex(request.getPassword());
        //logger.info("Hashed password: {} and DB password {}", hashed,  request.getPassword());
        if (!hashed.equals(user.getPassword())) {
            // increment fail count
            int fails = user.getPasswordFailCount() == null ? 0 : user.getPasswordFailCount();
            fails++;
            user.setPasswordFailCount(fails);
            if (fails > maxFailedAttempts) {
                user.setUserLocked("Y");
                userRepository.save(user);
                logger.warn("User locked due to failed attempts: {}", request.getUsername());
                return AuthResponse.error("User account locked. Contact administrator.");
            }
            userRepository.save(user);
            logger.warn("Invalid password for user: {}", request.getUsername());
            return AuthResponse.error("Invalid credentials");
        }

        // success
        user.setPasswordFailCount(0);
        userRepository.save(user);

        // enforce maximum 2 active sessions per user
        long activeCount = sessionRepository.countByUserIdAndStatus(user.getUserId(), "ACTIVE");
        if (activeCount >= 2) {
            logger.warn("User {} has reached max active sessions ({})", user.getUsername(), activeCount);
            return AuthResponse.error("Maximum active sessions reached");
        }

        String token = jwtUtil.generateToken(user.getUserId(), user.getUsername(), user.getRole(), user.getAppId());

        // generate refresh token and store session
        String refreshToken = UUID.randomUUID().toString();
        UserSessionEntity session = new UserSessionEntity();
        session.setSessionKey(token);
        session.setAppId(user.getAppId());
        session.setUserId(user.getUserId());
        Instant now = Instant.now();
        session.setCreatedAt(now);
        session.setExpireAt(now.plusSeconds(jwtExpiration));
        session.setRefreshToken(refreshToken);
        session.setRefreshExpireAt(now.plusSeconds(refreshExpiration));
        session.setDeviceInfo(request.getDeviceInfo());
        session.setStatus("ACTIVE");
        sessionRepository.save(session);

        // add active user record
        try {
            ActiveUserEntity active = new ActiveUserEntity();
            active.setSessionKey(token);
            active.setAppId(user.getAppId());
            active.setUserId(user.getUserId());
            active.setUsername(user.getUsername());
            active.setRole(user.getRole());
            active.setLoginAt(now);
            active.setExpireAt(now.plusSeconds(jwtExpiration));
            active.setDeviceInfo(request.getDeviceInfo());
            active.setStatus("ACTIVE");
            activeUserRepository.save(active);
        } catch (Exception e) {
            logger.warn("Active user creation failed: {}", e.getMessage());
        }

        logger.info("Token generated for user: {}", user.getUsername());

        return AuthResponse.successWithRefresh(user.getUserId(), user.getRole(), token, String.valueOf(jwtExpiration), refreshToken);
    }

    @Transactional
    public AuthResponse refresh(String refreshToken) {
        logger.info("refresh() called for refreshToken={}", refreshToken);
        if (refreshToken == null || refreshToken.isBlank()) {
            logger.warn("refresh() invalid token");
            return AuthResponse.error("Invalid refresh token");
        }
        java.util.Optional<UserSessionEntity> sessionOpt = sessionRepository.findByRefreshToken(refreshToken);
        if (sessionOpt.isEmpty()) {
            logger.warn("refresh() token not found");
            return AuthResponse.error("Refresh token not found");
        }
        UserSessionEntity oldSession = sessionOpt.get();
        if (!"ACTIVE".equalsIgnoreCase(oldSession.getStatus())) {
            logger.warn("refresh() session not active for userId={}", oldSession.getUserId());
            return AuthResponse.error("Session not active");
        }
        Instant now = Instant.now();
        if (oldSession.getRefreshExpireAt() == null || now.isAfter(oldSession.getRefreshExpireAt())) {
            logger.warn("refresh() token expired for userId={}", oldSession.getUserId());
            return AuthResponse.error("Refresh token expired");
        }

        java.util.Optional<UserEntity> userOpt = userRepository.findById(oldSession.getUserId());
        if (userOpt.isEmpty()) {
            logger.warn("refresh() user not found userId={}", oldSession.getUserId());
            return AuthResponse.error("User not found");
        }
        UserEntity user = userOpt.get();

        // create new tokens and new session, delete old session
        String newToken = jwtUtil.generateToken(user.getUserId(), user.getUsername(), user.getRole(), user.getAppId());
        String newRefresh = UUID.randomUUID().toString();

        UserSessionEntity newSession = new UserSessionEntity();
        newSession.setSessionKey(newToken);
        newSession.setAppId(user.getAppId());
        newSession.setUserId(user.getUserId());
        newSession.setCreatedAt(now);
        newSession.setExpireAt(now.plusSeconds(jwtExpiration));
        newSession.setRefreshToken(newRefresh);
        newSession.setRefreshExpireAt(now.plusSeconds(refreshExpiration));
        newSession.setDeviceInfo(oldSession.getDeviceInfo());
        newSession.setStatus("ACTIVE");
        sessionRepository.save(newSession);
        // update active user: remove old and add new
        try {
            activeUserRepository.deleteById(oldSession.getSessionKey());
        } catch (Exception ignore) {}
        try {
            ActiveUserEntity active = new ActiveUserEntity();
            active.setSessionKey(newToken);
            active.setAppId(user.getAppId());
            active.setUserId(user.getUserId());
            active.setUsername(user.getUsername());
            active.setRole(user.getRole());
            active.setLoginAt(now);
            active.setExpireAt(now.plusSeconds(jwtExpiration));
            active.setDeviceInfo(oldSession.getDeviceInfo());
            active.setStatus("ACTIVE");
            activeUserRepository.save(active);
        } catch (Exception e) {
            logger.warn("Failed to update active user on refresh: {}", e.getMessage());
        }
        sessionRepository.delete(oldSession);

        logger.info("refresh() success for userId={}", user.getUserId());
        return AuthResponse.successWithRefresh(user.getUserId(), user.getRole(), newToken, String.valueOf(jwtExpiration), newRefresh);
    }

    public boolean validateSession(String token) {
        logger.info("validateSession() called for token (masked)={}", token == null ? null : (token.length() > 8 ? token.substring(0,8) + "..." : token));
        if (token == null || token.isBlank()) {
            logger.warn("validateSession() invalid token");
            return false;
        }
        java.util.Optional<UserSessionEntity> s = sessionRepository.findById(token);
        if (s.isEmpty()) {
            logger.warn("validateSession() session not found");
            return false;
        }
        UserSessionEntity ses = s.get();
        if (!"ACTIVE".equalsIgnoreCase(ses.getStatus())) {
            logger.warn("validateSession() session not active for userId={}", ses.getUserId());
            return false;
        }
        Instant now = Instant.now();
        boolean valid = ses.getExpireAt() != null && now.isBefore(ses.getExpireAt());
        logger.info("validateSession() result={} for userId={}", valid, ses.getUserId());
        return valid;
    }

    @Transactional
    public boolean logout(String token) {
        if (token == null || token.isBlank()) return false;
        try {
            java.util.Optional<UserSessionEntity> s = sessionRepository.findById(token);
            if (s.isEmpty()) return false;
            UserSessionEntity ses = s.get();
            // delete session
            sessionRepository.delete(ses);
            // delete active user record
            try {
                activeUserRepository.deleteById(token);
            } catch (Exception ignore) {}
            logger.info("logout() success for userId={}", ses.getUserId());
            return true;
        } catch (Exception e) {
            logger.error("logout() failed: {}", e.getMessage());
            return false;
        }
    }

    private String sha256Hex(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString().toLowerCase();
        } catch (Exception e) {
            throw new RuntimeException("Failed to hash password", e);
        }
    }
}
