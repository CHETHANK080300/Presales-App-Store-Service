package com.iexceed.appzillon.appstore.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppMasterResponseDto {

    private String status;
    private String message;
    private String appId;
    private String appName;
    private String bundleId;
    private String platform;
    private String category;
    private String accessGroup;
    private String appVersion;
    private String description;
    private String releaseNotes;
    private String apkDownloadUrl;
    private String ipaDownloadUrl;
    private String plistDownloadUrl;
    private String imageDownloadUrl;
    private String androidQrUrl;
    private String iosQrUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
