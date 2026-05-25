package com.iexceed.appzillon.appstore.controller;

import com.iexceed.appzillon.appstore.repository.ActiveUserRepository;
import com.iexceed.appzillon.appstore.dto.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/active-users")
public class ActiveUserController {

    @Autowired
    private ActiveUserRepository activeUserRepository;

    @GetMapping
    public ResponseEntity<CommonResponse> list(@RequestParam(value = "role", required = false) String role) {
        if (role == null || role.isBlank() || "ALL".equalsIgnoreCase(role)) {
            return ResponseEntity.ok(CommonResponse.success("Active users fetched", Map.of("list", activeUserRepository.findAll())));
        }
        return ResponseEntity.ok(CommonResponse.success("Active users fetched", Map.of("list", activeUserRepository.findByRole(role))));
    }

    @GetMapping("/count")
    public ResponseEntity<CommonResponse> count(@RequestParam(value = "role", required = false) String role) {
        if (role == null || role.isBlank() || "ALL".equalsIgnoreCase(role)) {
            long total = activeUserRepository.count();
            return ResponseEntity.ok(CommonResponse.success("Count fetched", Map.of("role", "ALL", "count", total)));
        }
        long cnt = activeUserRepository.countByRole(role);
        return ResponseEntity.ok(CommonResponse.success("Count fetched", Map.of("role", role, "count", cnt)));
    }
}
