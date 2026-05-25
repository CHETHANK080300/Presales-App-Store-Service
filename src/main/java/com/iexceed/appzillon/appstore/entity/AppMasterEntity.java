package com.iexceed.appzillon.appstore.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_asmi_app_master")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppMasterEntity {

    @Id
    private String id;

    @Column(name = "app_name")
    private String appName;

    @Column(name = "bundle_id")
    private String bundleId;

    @Column(name = "platform")
    private String platform;

    @Column(name = "category")
    private String category;

    @Column(name = "access_group")
    private String accessGroup;

    @Column(name = "app_expiry_set")
    private String appExpirySet;

    @Column(name = "app_expiry")
    private LocalDate appExpiry;

    @Column(name = "min_os_ver_android")
    private String minOsVerAndroid;

    @Column(name = "min_os_ver_ios")
    private String minOsVerIos;

    @Column(name = "description")
    private String description;

    @Column(name = "release_notes")
    private String releaseNotes;

    @Column(name = "apk_file_path")
    private String apkFilePath;

    @Column(name = "ipa_file_path")
    private String ipaFilePath;

    @Column(name = "plist_file_path")
    private String plistFilePath;

    @Column(name = "apk_download_url")
    private String apkDownloadUrl;

    @Column(name = "ipa_download_url")
    private String ipaDownloadUrl;

    @Column(name = "plist_download_url")
    private String plistDownloadUrl;

    @Column(name = "image_download_url")
    private String imageDownloadUrl;

    @Column(name = "android_qr_path")
    private String androidQrPath;

    @Column(name = "ios_qr_path")
    private String iosQrPath;

    @Column(name = "android_qr_url")
    private String androidQrUrl;

    @Column(name = "ios_qr_url")
    private String iosQrUrl;

    @Column(name = "app_logo")
    private String appLogo;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}