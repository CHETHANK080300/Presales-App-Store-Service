package com.iexceed.appzillon.appstore.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "TB_ASMI_USER_SESSION")
public class UserSessionEntity {

    @Id
    @Column(name = "session_key", length = 512)
    private String sessionKey;

    @Column(name = "app_id", length = 50)
    private String appId;

    @Column(name = "user_id", length = 50)
    private String userId;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "expire_at")
    private Instant expireAt;

    @Column(name = "refresh_token", length = 200)
    private String refreshToken;

    @Column(name = "refresh_expire_at")
    private Instant refreshExpireAt;

    @Column(name = "device_info", length = 200)
    private String deviceInfo;

    @Column(name = "status", length = 20)
    private String status;

    // Getters and setters
    public String getSessionKey() { return sessionKey; }
    public void setSessionKey(String sessionKey) { this.sessionKey = sessionKey; }
    public String getAppId() { return appId; }
    public void setAppId(String appId) { this.appId = appId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getExpireAt() { return expireAt; }
    public void setExpireAt(Instant expireAt) { this.expireAt = expireAt; }
    public String getDeviceInfo() { return deviceInfo; }
    public void setDeviceInfo(String deviceInfo) { this.deviceInfo = deviceInfo; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    public Instant getRefreshExpireAt() { return refreshExpireAt; }
    public void setRefreshExpireAt(Instant refreshExpireAt) { this.refreshExpireAt = refreshExpireAt; }
}
