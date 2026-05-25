package com.iexceed.appzillon.appstore.service.impl;

import com.iexceed.appzillon.appstore.dto.request.AppMasterRequestDto;
import com.iexceed.appzillon.appstore.dto.response.AppMasterResponseDto;
import com.iexceed.appzillon.appstore.entity.AppMasterEntity;
import com.iexceed.appzillon.appstore.exception.FileStorageException;
import com.iexceed.appzillon.appstore.exception.ResourceAlreadyExistsException;
import com.iexceed.appzillon.appstore.repository.AppMasterRepository;
import com.iexceed.appzillon.appstore.service.AppMasterService;
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

    @Override
    @Transactional
    public AppMasterResponseDto uploadApplication(
            AppMasterRequestDto requestDto,
            MultipartFile apkFile,
            MultipartFile ipaFile,
            MultipartFile plistFile,
            MultipartFile imageFile) {

        validateRequest(requestDto);

        String appId = generateAppId();

        String apkPath = null;
        String ipaPath = null;
        String imagePath = null;

        try {

            Path appBasePath = Paths.get(basePath, appId);

            Files.createDirectories(appBasePath);

            // APK
            if (apkFile != null && !apkFile.isEmpty()) {

                Path apkDir = appBasePath.resolve("apk");
                Files.createDirectories(apkDir);

                Path apkTarget = apkDir.resolve("apkFile.apk");

                Files.copy(apkFile.getInputStream(),
                        apkTarget,
                        StandardCopyOption.REPLACE_EXISTING);

                apkPath = apkTarget.toString();
            }

            // IPA + PLIST
            if (ipaFile != null && !ipaFile.isEmpty()) {

                Path ipaDir = appBasePath.resolve("ipa");
                Files.createDirectories(ipaDir);

                Path ipaTarget = ipaDir.resolve("ipaFile.ipa");

                Files.copy(ipaFile.getInputStream(),
                        ipaTarget,
                        StandardCopyOption.REPLACE_EXISTING);

                ipaPath = ipaTarget.toString();

                if (plistFile != null && !plistFile.isEmpty()) {

                    Path plistTarget = ipaDir.resolve("ipaPlistFile.plist");

                    Files.copy(plistFile.getInputStream(),
                            plistTarget,
                            StandardCopyOption.REPLACE_EXISTING);
                }
            }

            // Image
            if (imageFile != null && !imageFile.isEmpty()) {

                Path imageTarget = appBasePath.resolve("image.png");

                Files.copy(imageFile.getInputStream(),
                        imageTarget,
                        StandardCopyOption.REPLACE_EXISTING);

                imagePath = imageTarget.toString();
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

    private String generateAppId() {

        return "APP-" + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 10)
                .toUpperCase();
    }
}
