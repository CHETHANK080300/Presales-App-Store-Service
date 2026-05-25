package com.iexceed.appzillon.appstore.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppMasterRequestDto {

    @NotBlank
    private String appName;

    @NotBlank
    private String bundleId;

    @NotBlank
    private String platform;

    @NotBlank
    private String category;

    @NotBlank
    private String accessGroup;

    @NotBlank
    private String appExpirySet;

    private LocalDate expirationDate;

    private String minOsVerAndroid;

    private String minOsVerIos;

    private String appDescription;

    private String releaseNotes;

    private String createdBy;
}