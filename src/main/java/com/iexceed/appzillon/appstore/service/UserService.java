package com.iexceed.appzillon.appstore.service;

import com.iexceed.appzillon.appstore.dto.UserRequest;
import com.iexceed.appzillon.appstore.entity.UserEntity;
import com.iexceed.appzillon.appstore.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public Map<String, Object> createUser(String actorUserId, UserRequest req) {
        logger.debug("UserService.createUser actor={} username={}", actorUserId, req.getUsername());
        Map<String, Object> resp = new HashMap<>();

        if (req.getUsername() == null || req.getUsername().isBlank()) {
            resp.put("error", "username required");
            return resp;
        }

        String base = req.getUsername().toLowerCase().replaceAll("\\s+", "-");
        String userId = base;
        int i = 0;
        while (userRepository.findById(userId).isPresent()) {
            i++;
            userId = base + "-" + i;
        }

        UserEntity u = new UserEntity();
        u.setUserId(userId);
        u.setAppId(req.getAppId());
        u.setUsername(req.getUsername());
        // hash password
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(req.getPassword().getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            u.setPassword(sb.toString().toLowerCase());
        } catch (Exception e) {
            logger.error("createUser password hash failed", e);
            resp.put("error", "Failed to hash password");
            return resp;
        }

        u.setRole(req.getRole());
        u.setEmailId(req.getEmailId());
        u.setPhoneNo(req.getPhoneNo());
        u.setUserGroup(req.getUserGroup());
        u.setAuthorisedStatus(req.getAuthorisedStatus() == null ? "U" : req.getAuthorisedStatus());
        u.setUserStatus("ACTIVE");
        u.setCreatedAt(Instant.now());
        u.setCreatedBy(actorUserId);

        userRepository.save(u);
        resp.put("status", "CREATED");
        resp.put("userId", userId);
        return resp;
    }

    @Transactional
    public Map<String, Object> updateUser(String actorUserId, String userId, UserRequest req) {
        logger.debug("UserService.updateUser actor={} userId={}", actorUserId, userId);
        Map<String, Object> resp = new HashMap<>();
        var opt = userRepository.findById(userId);
        if (opt.isEmpty()) {
            resp.put("error", "not_found");
            return resp;
        }
        UserEntity u = opt.get();
        if (req.getPassword() != null && !req.getPassword().isBlank()) {
            try {
                java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(req.getPassword().getBytes(java.nio.charset.StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder();
                for (byte b : hash) sb.append(String.format("%02x", b));
                u.setPassword(sb.toString().toLowerCase());
            } catch (Exception e) {
                logger.error("updateUser password hash failed", e);
                resp.put("error", "Failed to hash password");
                return resp;
            }
        }
        if (req.getRole() != null) u.setRole(req.getRole());
        if (req.getEmailId() != null) u.setEmailId(req.getEmailId());
        if (req.getPhoneNo() != null) u.setPhoneNo(req.getPhoneNo());
        if (req.getUserGroup() != null) u.setUserGroup(req.getUserGroup());
        if (req.getAuthorisedStatus() != null) u.setAuthorisedStatus(req.getAuthorisedStatus());
        u.setUpdatedAt(Instant.now());
        userRepository.save(u);
        resp.put("status", "UPDATED");
        resp.put("userId", userId);
        return resp;
    }

    @Transactional
    public Map<String, Object> deleteUser(String actorUserId, String userId) {
        logger.debug("UserService.deleteUser actor={} userId={}", actorUserId, userId);
        Map<String, Object> resp = new HashMap<>();
        if (!userRepository.existsById(userId)) {
            resp.put("error", "not_found");
            return resp;
        }
        userRepository.deleteById(userId);
        resp.put("status", "DELETED");
        resp.put("userId", userId);
        return resp;
    }

    @Transactional
    public Map<String, Object> blockUser(String actorUserId, String userId) {
        logger.debug("UserService.blockUser actor={} userId={}", actorUserId, userId);
        Map<String, Object> resp = new HashMap<>();
        var opt = userRepository.findById(userId);
        if (opt.isEmpty()) {
            resp.put("error", "not_found");
            return resp;
        }
        UserEntity u = opt.get();
        u.setUserStatus("BLOCKED");
        userRepository.save(u);
        resp.put("status", "BLOCKED");
        resp.put("userId", userId);
        return resp;
    }

    @Transactional
    public Map<String, Object> unblockUser(String actorUserId, String userId) {
        logger.debug("UserService.unblockUser actor={} userId={}", actorUserId, userId);
        Map<String, Object> resp = new HashMap<>();
        var opt = userRepository.findById(userId);
        if (opt.isEmpty()) {
            resp.put("error", "not_found");
            return resp;
        }
        UserEntity u = opt.get();
        u.setUserStatus("ACTIVE");
        userRepository.save(u);
        resp.put("status", "UNBLOCKED");
        resp.put("userId", userId);
        return resp;
    }

    @Transactional
    public Map<String, Object> fetchAllUsers() {
        logger.debug("UserService.fetchAllUsers");
        Map<String, Object> resp = new HashMap<>();
        List<UserEntity> users = userRepository.findAll();
        /*users.forEach(user -> {
            user.setPassword(null);
        });*/
        resp.put("users", users);
        return resp;
    }
}
