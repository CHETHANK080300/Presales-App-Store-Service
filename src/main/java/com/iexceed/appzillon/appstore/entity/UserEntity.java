package com.iexceed.appzillon.appstore.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "TB_ASMI_USER")
public class UserEntity {

    @Id
    @Column(name = "user_id", length = 50)
    private String userId;

    @Column(name = "app_id", length = 50)
    private String appId;

    @Column(name = "username", length = 100, unique = true)
    private String username;

    @Column(name = "password", length = 256)
    private String password;

    @Column(name = "role", length = 50)
    private String role;

    @Column(name = "email_id", length = 100)
    private String emailId;

    @Column(name = "phone_no", length = 15)
    private String phoneNo;

    @Column(name = "user_locked", length = 1)
    private String userLocked;

    @Column(name = "password_fail_count")
    private Integer passwordFailCount;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "user_group", length = 100)
    private String userGroup;

    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "authorised_by", length = 50)
    private String authorisedBy;

    @Column(name = "user_status", length = 20)
    private String userStatus; // ACTIVE or BLOCKED

    @Column(name = "authorised_status", length = 1)
    private String authorisedStatus; // A or U

    // Getters and setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getAppId() { return appId; }
    public void setAppId(String appId) { this.appId = appId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getEmailId() { return emailId; }
    public void setEmailId(String emailId) { this.emailId = emailId; }
    public String getPhoneNo() { return phoneNo; }
    public void setPhoneNo(String phoneNo) { this.phoneNo = phoneNo; }
    public String getUserLocked() { return userLocked; }
    public void setUserLocked(String userLocked) { this.userLocked = userLocked; }
    public Integer getPasswordFailCount() { return passwordFailCount; }
    public void setPasswordFailCount(Integer passwordFailCount) { this.passwordFailCount = passwordFailCount; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public String getUserGroup() { return userGroup; }
    public void setUserGroup(String userGroup) { this.userGroup = userGroup; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public String getAuthorisedBy() { return authorisedBy; }
    public void setAuthorisedBy(String authorisedBy) { this.authorisedBy = authorisedBy; }
    public String getUserStatus() { return userStatus; }
    public void setUserStatus(String userStatus) { this.userStatus = userStatus; }
    public String getAuthorisedStatus() { return authorisedStatus; }
    public void setAuthorisedStatus(String authorisedStatus) { this.authorisedStatus = authorisedStatus; }
}
