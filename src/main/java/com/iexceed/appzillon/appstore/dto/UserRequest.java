package com.iexceed.appzillon.appstore.dto;

public class UserRequest {
    private String username;
    private String password;
    private String role;
    private String emailId;
    private String phoneNo;
    private String appId;
    private String userGroup;
    private String authorisedStatus; // A or U

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
    public String getAppId() { return appId; }
    public void setAppId(String appId) { this.appId = appId; }
    public String getUserGroup() { return userGroup; }
    public void setUserGroup(String userGroup) { this.userGroup = userGroup; }
    public String getAuthorisedStatus() { return authorisedStatus; }
    public void setAuthorisedStatus(String authorisedStatus) { this.authorisedStatus = authorisedStatus; }
}
