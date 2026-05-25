package com.iexceed.appzillon.appstore.controller;

import com.iexceed.appzillon.appstore.dto.UserRequest;
import com.iexceed.appzillon.appstore.dto.CommonResponse;
import com.iexceed.appzillon.appstore.repository.UserRepository;
import com.iexceed.appzillon.appstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private boolean isAdmin() {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        if (a == null) return false;
        return a.getAuthorities().stream().anyMatch(g -> g.getAuthority().equalsIgnoreCase("ADMIN"));
    }

    private String currentUserId() {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        return a == null ? null : String.valueOf(a.getPrincipal());
    }

    @PostMapping("/create")
    public ResponseEntity<CommonResponse> createUser(@RequestBody UserRequest req) {
        logger.info("createUser called by {} with username={}", currentUserId(), req.getUsername());
        if (!isAdmin()) {
            logger.warn("createUser forbidden for {}", currentUserId());
            return ResponseEntity.status(403).body(CommonResponse.failure("Forbidden"));
        }
        var result = userService.createUser(currentUserId(), req);
        if (result.containsKey("error")) return ResponseEntity.badRequest().body(CommonResponse.failure(String.valueOf(result.get("error"))));
        return ResponseEntity.ok(CommonResponse.success("User created", result));
    }

    @GetMapping("/fetch")
    public ResponseEntity<CommonResponse> fetchUser() {
        logger.info("fetchUser called by {} with username={}", currentUserId(), currentUserId());
        if (!isAdmin()) {
            logger.warn("fetchUser forbidden for {}", currentUserId());
            return ResponseEntity.status(403).body(CommonResponse.failure("Forbidden"));
        }
        Map<String, Object> resp = userService.fetchAllUsers();
        return ResponseEntity.ok(CommonResponse.success("Users Fetched Successfully", resp));
    }

    @PostMapping("/update/{userId}")
    public ResponseEntity<CommonResponse> updateUser(@PathVariable String userId, @RequestBody UserRequest req) {
        logger.info("updateUser called by {} for userId={}", currentUserId(), userId);
        if (!isAdmin()) {
            logger.warn("updateUser forbidden for {}", currentUserId());
            return ResponseEntity.status(403).body(CommonResponse.failure("Forbidden"));
        }
        var result = userService.updateUser(currentUserId(), userId, req);
        if (result.containsKey("error")) {
            if ("not_found".equals(result.get("error"))) return ResponseEntity.notFound().build();
            return ResponseEntity.status(500).body(CommonResponse.failure(String.valueOf(result.get("error"))));
        }
        return ResponseEntity.ok(CommonResponse.success("User updated", result));
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<CommonResponse> deleteUser(@PathVariable String userId) {
        logger.info("deleteUser called by {} for userId={}", currentUserId(), userId);
        if (!isAdmin()) {
            logger.warn("deleteUser forbidden for {}", currentUserId());
            return ResponseEntity.status(403).body(CommonResponse.failure("Forbidden"));
        }
        var result = userService.deleteUser(currentUserId(), userId);
        if (result.containsKey("error")) {
            if ("not_found".equals(result.get("error"))) return ResponseEntity.notFound().build();
            return ResponseEntity.status(500).body(CommonResponse.failure(String.valueOf(result.get("error"))));
        }
        return ResponseEntity.ok(CommonResponse.success("User deleted", result));
    }

    @PostMapping("/block/{userId}")
    public ResponseEntity<CommonResponse> blockUser(@PathVariable String userId) {
        logger.info("blockUser called by {} for userId={}", currentUserId(), userId);
        if (!isAdmin()) {
            logger.warn("blockUser forbidden for {}", currentUserId());
            return ResponseEntity.status(403).body(CommonResponse.failure("Forbidden"));
        }
        var result = userService.blockUser(currentUserId(), userId);
        if (result.containsKey("error")) {
            if ("not_found".equals(result.get("error"))) return ResponseEntity.notFound().build();
            return ResponseEntity.status(500).body(CommonResponse.failure(String.valueOf(result.get("error"))));
        }
        return ResponseEntity.ok(CommonResponse.success("User blocked", result));
    }

    @PostMapping("/unblock/{userId}")
    public ResponseEntity<CommonResponse> unblockUser(@PathVariable String userId) {
        logger.info("unblockUser called by {} for userId={}", currentUserId(), userId);
        if (!isAdmin()) {
            logger.warn("unblockUser forbidden for {}", currentUserId());
            return ResponseEntity.status(403).body(CommonResponse.failure("Forbidden"));
        }
        var result = userService.unblockUser(currentUserId(), userId);
        if (result.containsKey("error")) {
            if ("not_found".equals(result.get("error"))) return ResponseEntity.notFound().build();
            return ResponseEntity.status(500).body(CommonResponse.failure(String.valueOf(result.get("error"))));
        }
        return ResponseEntity.ok(CommonResponse.success("User unblocked", result));
    }
}
