package com.iexceed.appzillon.appstore.service.impl;

import com.iexceed.appzillon.appstore.dto.request.AppMasterRequestDto;
import com.iexceed.appzillon.appstore.dto.response.AppMasterResponseDto;
import com.iexceed.appzillon.appstore.entity.AppMasterEntity;
import com.iexceed.appzillon.appstore.exception.FileStorageException;
import com.iexceed.appzillon.appstore.exception.ResourceAlreadyExistsException;
import com.iexceed.appzillon.appstore.repository.AppMasterRepository;
import com.iexceed.appzillon.appstore.service.AppMasterService;
import com.iexceed.appzillon.appstore.util.FileValidationUtils;
import com.iexceed.appzillon.appstore.util.PlistUtils;
import com.iexceed.appzillon.appstore.util.QrCodeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppMasterServiceImpl implements AppMasterService {

    private final AppMasterRepository repository;

    @Value("${app.upload.base-path}")
    private String basePath;

    @Value("${app.download.base-url}")
    private String downloadBaseUrl;

    @Value("${app.download.context-path}")
    private String downloadContextPath;

    @Value("${app.qr.size:250}")
    private int qrSize;

    @Override
    @Transactional
    public AppMasterResponseDto uploadApplication(
            AppMasterRequestDto requestDto,
            MultipartFile apkFile,
            MultipartFile ipaFile,
            MultipartFile plistFile,
            MultipartFile imageFile) {

        validateRequest(requestDto);
        validateFiles(apkFile, ipaFile, imageFile);

        String appId = generateAppId();

        String apkPath = null;
        String ipaPath = null;
        String plistPath = null;
        String imagePath = null;
        String androidQrPath = null;
        String iosQrPath = null;

        String apkDownloadUrl = null;
        String ipaDownloadUrl = null;
        String plistDownloadUrl = null;
        String imageDownloadUrl = null;
        String androidQrUrl = null;
        String iosQrUrl = null;

        try {
            Path appBasePath = Paths.get(basePath, appId);
            Files.createDirectories(appBasePath);

            // APK
            if (apkFile != null && !apkFile.isEmpty()) {
                Path apkDir = appBasePath.resolve("apk");
                Files.createDirectories(apkDir);
                Path apkTarget = apkDir.resolve("apkFile.apk");
                Files.copy(apkFile.getInputStream(), apkTarget, StandardCopyOption.REPLACE_EXISTING);
                apkPath = apkTarget.toString();
                apkDownloadUrl = buildDownloadUrl(appId, "apk/apkFile.apk");

                // Android QR
                Path qrDir = appBasePath.resolve("qr");
                Files.createDirectories(qrDir);
                Path androidQrTarget = qrDir.resolve("android_qr.png");
                QrCodeUtils.generateQrCode(apkDownloadUrl, qrSize, qrSize, androidQrTarget);
                androidQrPath = androidQrTarget.toString();
                androidQrUrl = buildDownloadUrl(appId, "qr/android_qr.png");
            }

            // IPA + PLIST
            if (ipaFile != null && !ipaFile.isEmpty()) {
                Path ipaDir = appBasePath.resolve("ipa");
                Files.createDirectories(ipaDir);
                Path ipaTarget = ipaDir.resolve("ipaFile.ipa");
                Files.copy(ipaFile.getInputStream(), ipaTarget, StandardCopyOption.REPLACE_EXISTING);
                ipaPath = ipaTarget.toString();
                ipaDownloadUrl = buildDownloadUrl(appId, "ipa/ipaFile.ipa");

                Path plistTarget = ipaDir.resolve("ipaPlistFile.plist");
                String dynamicPlistDownloadUrl = buildDownloadUrl(appId, "ipa/ipaPlistFile.plist");
                PlistUtils.generateIpaPlist(
                        plistTarget,
                        requestDto.getBundleId(),
                        requestDto.getAppVersion() != null ? requestDto.getAppVersion() : "1.0.0",
                        requestDto.getAppName(),
                        ipaDownloadUrl
                );
                plistPath = plistTarget.toString();
                plistDownloadUrl = "itms-services://?action=download-manifest&url=" + dynamicPlistDownloadUrl;

                // iOS QR
                Path qrDir = appBasePath.resolve("qr");
                Files.createDirectories(qrDir);
                Path iosQrTarget = qrDir.resolve("ios_qr.png");
                QrCodeUtils.generateQrCode(plistDownloadUrl, qrSize, qrSize, iosQrTarget);
                iosQrPath = iosQrTarget.toString();
                iosQrUrl = buildDownloadUrl(appId, "qr/ios_qr.png");
            }

            // Image
            if (imageFile != null && !imageFile.isEmpty()) {
                String originalFilename = imageFile.getOriginalFilename();
                String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                Path imageTarget = appBasePath.resolve("image" + extension);
                Files.copy(imageFile.getInputStream(), imageTarget, StandardCopyOption.REPLACE_EXISTING);
                imagePath = imageTarget.toString();
                imageDownloadUrl = buildDownloadUrl(appId, "image" + extension);
            }

            AppMasterEntity entity = AppMasterEntity.builder()
                    .id(appId)
                    .appName(requestDto.getAppName())
                    .bundleId(requestDto.getBundleId())
                    .platform(requestDto.getPlatform())
                    .category(requestDto.getCategory())
                    .accessGroup(requestDto.getAccessGroup())
                    .appExpirySet(requestDto.getAppExpirySet())
                    .appExpiry(requestDto.getExpirationDate())
                    .minOsVerAndroid(requestDto.getMinOsVerAndroid())
                    .minOsVerIos(requestDto.getMinOsVerIos())
                    .appVersion(requestDto.getAppVersion() != null ? requestDto.getAppVersion() : "1.0.0")
                    .description(requestDto.getAppDescription())
                    .releaseNotes(requestDto.getReleaseNotes())
                    .apkFilePath(apkPath)
                    .ipaFilePath(ipaPath)
                    .plistFilePath(plistPath)
                    .apkDownloadUrl(apkDownloadUrl)
                    .ipaDownloadUrl(ipaDownloadUrl)
                    .plistDownloadUrl(plistDownloadUrl)
                    .imageDownloadUrl(imageDownloadUrl)
                    .androidQrPath(androidQrPath)
                    .iosQrPath(iosQrPath)
                    .androidQrUrl(androidQrUrl)
                    .iosQrUrl(iosQrUrl)
                    .appLogo(imagePath)
                    .createdBy(requestDto.getCreatedBy())
                    .updatedBy(requestDto.getCreatedBy())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            repository.save(entity);

            return mapToResponseDto(entity, "SUCCESS", "Application uploaded successfully");

        } catch (Exception ex) {
            log.error("Failed to store files", ex);
            throw new FileStorageException("Failed to store files", ex);
        }
    }

    @Override
    public List<AppMasterResponseDto> getAppList(String accessGroup) {
        List<AppMasterEntity> entities;
        if ("Admin".equalsIgnoreCase(accessGroup)) {
            entities = repository.findAllByOrderByCreatedAtDesc();
        } else {
            entities = repository.findByAccessGroupOrderByCreatedAtDesc(accessGroup);
        }

        return entities.stream()
                .map(entity -> mapToResponseDto(entity, null, null))
                .collect(Collectors.toList());
    }

    private void validateRequest(AppMasterRequestDto requestDto) {
        if (repository.existsByBundleId(requestDto.getBundleId())) {
            throw new ResourceAlreadyExistsException(
                    "Application already exists with Bundle Id : "
                            + requestDto.getBundleId());
        }

        if ("Y".equalsIgnoreCase(requestDto.getAppExpirySet())
                && requestDto.getExpirationDate() == null) {
            throw new IllegalArgumentException(
                    "Expiration date is mandatory when expiry is enabled");
        }
    }

    private void validateFiles(MultipartFile apkFile, MultipartFile ipaFile, MultipartFile imageFile) {
        FileValidationUtils.validateApk(apkFile);
        FileValidationUtils.validateIpa(ipaFile);
        FileValidationUtils.validateImage(imageFile);
    }

    private String generateAppId() {
        return "APP-" + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 10)
                .toUpperCase();
    }

    private String buildDownloadUrl(String appId, String filePath) {
        return downloadBaseUrl + downloadContextPath + "/" + appId + "/" + filePath;
    }

    private AppMasterResponseDto mapToResponseDto(AppMasterEntity entity, String status, String message) {
        return AppMasterResponseDto.builder()
                .status(status)
                .message(message)
                .appId(entity.getId())
                .appName(entity.getAppName())
                .bundleId(entity.getBundleId())
                .platform(entity.getPlatform())
                .category(entity.getCategory())
                .accessGroup(entity.getAccessGroup())
                .appVersion(entity.getAppVersion())
                .description(entity.getDescription())
                .releaseNotes(entity.getReleaseNotes())
                .apkDownloadUrl(entity.getApkDownloadUrl())
                .ipaDownloadUrl(entity.getIpaDownloadUrl())
                .plistDownloadUrl(entity.getPlistDownloadUrl())
                .imageDownloadUrl(entity.getImageDownloadUrl())
                .androidQrUrl(entity.getAndroidQrUrl())
                .iosQrUrl(entity.getIosQrUrl())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
