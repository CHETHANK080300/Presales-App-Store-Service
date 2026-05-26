package com.iexceed.appzillon.appstore.dto.response;

import lombok.*;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {
    private String userId;
    private String appId;
    private String username;
    private String role;
    private String emailId;
    private String phoneNo;
    private String userLocked;
    private Integer passwordFailCount;
    private Instant createdAt;
    private Instant updatedAt;
    private String userGroup;
    private String createdBy;
    private String authorisedBy;
    private String userStatus;
    private String authorisedStatus;
}
