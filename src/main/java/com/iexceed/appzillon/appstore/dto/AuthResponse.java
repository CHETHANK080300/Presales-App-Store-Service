package com.iexceed.appzillon.appstore.dto;

public class AuthResponse {
    private String status;
    private String userId;
    private String username;
    private String userGroup;
    private String role;
    private String token;
    private String refreshToken;
    private String expiresIn;
    private String error;

    public static AuthResponse success(String userId, String username, String userGroup, String role, String token, String expiresIn) {
        AuthResponse r = new AuthResponse();
        r.status = "SUCCESS";
        r.userId = userId;
        r.username = username;
        r.userGroup = userGroup;
        r.role = role;
        r.token = token;
        r.expiresIn = expiresIn;
        return r;
    }

    public static AuthResponse successWithRefresh(String userId, String username, String userGroup, String role, String token, String expiresIn, String refreshToken) {
        AuthResponse r = new AuthResponse();
        r.status = "SUCCESS";
        r.userId = userId;
        r.username = username;
        r.userGroup = userGroup;
        r.role = role;
        r.token = token;
        r.expiresIn = expiresIn;
        r.refreshToken = refreshToken;
        return r;
    }

    public static AuthResponse error(String error) {
        AuthResponse r = new AuthResponse();
        r.status = "FAILED";
        r.error = error;
        return r;
    }

    // Getters
    public String getStatus() { return status; }
    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getUserGroup() { return userGroup; }
    public String getRole() { return role; }
    public String getToken() { return token; }
    public String getRefreshToken() { return refreshToken; }
    public String getExpiresIn() { return expiresIn; }
    public String getError() { return error; }
}
