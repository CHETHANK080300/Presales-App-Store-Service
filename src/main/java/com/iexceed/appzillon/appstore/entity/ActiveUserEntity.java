package com.iexceed.appzillon.appstore.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "TB_ASMI_ACTIVE_USER")
public class ActiveUserEntity {

    @Id
    @Column(name = "session_key", length = 512)
    private String sessionKey;

    @Column(name = "app_id", length = 50)
    private String appId;

    @Column(name = "user_id", length = 50)
    private String userId;

    @Column(name = "username", length = 100)
    private String username;

    @Column(name = "role", length = 50)
    private String role;

    @Column(name = "login_at")
    private Instant loginAt;

    @Column(name = "expire_at")
    private Instant expireAt;

    @Column(name = "device_info", length = 200)
    private String deviceInfo;

    @Column(name = "status", length = 20)
    private String status;

    public String getSessionKey() { return sessionKey; }
    public void setSessionKey(String sessionKey) { this.sessionKey = sessionKey; }
    public String getAppId() { return appId; }
    public void setAppId(String appId) { this.appId = appId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Instant getLoginAt() { return loginAt; }
    public void setLoginAt(Instant loginAt) { this.loginAt = loginAt; }
    public Instant getExpireAt() { return expireAt; }
    public void setExpireAt(Instant expireAt) { this.expireAt = expireAt; }
    public String getDeviceInfo() { return deviceInfo; }
    public void setDeviceInfo(String deviceInfo) { this.deviceInfo = deviceInfo; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
