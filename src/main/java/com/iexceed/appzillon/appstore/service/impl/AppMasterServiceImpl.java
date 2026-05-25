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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.UUID;

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

        String apkDownloadUrl = null;
        String ipaDownloadUrl = null;
        String plistDownloadUrl = null;
        String imageDownloadUrl = null;

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
                // Requirement 2: Backend must dynamically generate plist file after IPA upload
                // Even if plistFile is provided (backward compatibility), we can choose to overwrite or prioritize dynamic generation
                // Requirement 3: Generate {basePath}/{appId}/ipa/ipaPlistFile.plist

                String dynamicPlistDownloadUrl = buildDownloadUrl(appId, "ipa/ipaPlistFile.plist");
                PlistUtils.generateIpaPlist(
                        plistTarget,
                        requestDto.getBundleId(),
                        requestDto.getAppVersion() != null ? requestDto.getAppVersion() : "1.0.0",
                        requestDto.getAppName(),
                        ipaDownloadUrl
                );
                plistPath = plistTarget.toString();
                // Requirement 13: iOS OTA Support
                plistDownloadUrl = "itms-services://?action=download-manifest&url=" + dynamicPlistDownloadUrl;
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
                    .description(requestDto.getAppDescription())
                    .releaseNotes(requestDto.getReleaseNotes())
                    .apkFilePath(apkPath)
                    .ipaFilePath(ipaPath)
                    .plistFilePath(plistPath)
                    .apkDownloadUrl(apkDownloadUrl)
                    .ipaDownloadUrl(ipaDownloadUrl)
                    .plistDownloadUrl(plistDownloadUrl)
                    .imageDownloadUrl(imageDownloadUrl)
                    .appLogo(imagePath)
                    .createdBy(requestDto.getCreatedBy())
                    .updatedBy(requestDto.getCreatedBy())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            repository.save(entity);

            return AppMasterResponseDto.builder()
                    .status("SUCCESS")
                    .message("Application uploaded successfully")
                    .appId(appId)
                    .apkDownloadUrl(apkDownloadUrl)
                    .ipaDownloadUrl(ipaDownloadUrl)
                    .plistDownloadUrl(plistDownloadUrl)
                    .imageDownloadUrl(imageDownloadUrl)
                    .build();

        } catch (IOException ex) {
            throw new FileStorageException("Failed to store files", ex);
        }
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
}
