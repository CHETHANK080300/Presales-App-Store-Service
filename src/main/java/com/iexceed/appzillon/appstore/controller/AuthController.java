package com.iexceed.appzillon.appstore.controller;

import com.iexceed.appzillon.appstore.dto.AuthResponse;
import com.iexceed.appzillon.appstore.dto.CommonResponse;
import com.iexceed.appzillon.appstore.dto.LoginRequest;
import com.iexceed.appzillon.appstore.dto.RefreshRequest;
import com.iexceed.appzillon.appstore.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<CommonResponse> login(@RequestBody LoginRequest request) {
        logger.info("Login API called for userId={}", request != null ? request.getUserId() : "null");
        AuthResponse resp = authService.login(request);
        if (resp.getStatus() != null && resp.getStatus().equals("SUCCESS")) {
            logger.info("Login successful for userId={}", request.getUserId());
            return ResponseEntity.ok(CommonResponse.success("Login successful", resp));
        } else {
            logger.warn("Login failed for userId={} reason={}", request != null ? request.getUserId() : "null", resp.getError());
            return ResponseEntity.status(400).body(CommonResponse.failure(resp.getError()));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<CommonResponse> refresh(@RequestBody RefreshRequest request) {
        logger.info("Refresh API called");
        AuthResponse resp = authService.refresh(request.getRefreshToken());
        if (resp.getStatus() != null && resp.getStatus().equals("SUCCESS")) {
            logger.info("Refresh successful");
            return ResponseEntity.ok(CommonResponse.success("Refresh successful", resp));
        }
        logger.warn("Refresh failed reason={}", resp.getError());
        return ResponseEntity.status(401).body(CommonResponse.failure(resp.getError()));
    }

    @GetMapping("/validate-session")
    public ResponseEntity<CommonResponse> validateSession(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        logger.info("validateSession called");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("validateSession missing or invalid Authorization header");
            return ResponseEntity.status(401).body(CommonResponse.failure("Missing or invalid Authorization header"));
        }
        String token = authHeader.substring(7);
        boolean ok = authService.validateSession(token);
        logger.info("validateSession result={}", ok);
        if (ok) {
            return ResponseEntity.ok(CommonResponse.success("ACTIVE", java.util.Map.of("status","ACTIVE")));
        }
        return ResponseEntity.status(401).body(CommonResponse.failure("Token expired or invalid"));
    }

    @PostMapping("/logout")
    public ResponseEntity<CommonResponse> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        logger.info("logout called");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(CommonResponse.failure("Missing or invalid Authorization header"));
        }
        String token = authHeader.substring(7);
        boolean ok = authService.logout(token);
        if (ok) return ResponseEntity.ok(CommonResponse.success("LOGGED_OUT", null));
        return ResponseEntity.status(500).body(CommonResponse.failure("Logout failed"));
    }
}
