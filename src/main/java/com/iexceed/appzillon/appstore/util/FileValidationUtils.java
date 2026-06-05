package com.iexceed.appzillon.appstore.util;

import org.springframework.web.multipart.MultipartFile;
import java.util.Arrays;
import java.util.List;

public class FileValidationUtils {

    private static final List<String> IMAGE_EXTENSIONS = Arrays.asList("png", "jpg", "jpeg");

    public static void validateApk(MultipartFile file) {
        if (file == null || file.isEmpty()) return;
        String filename = file.getOriginalFilename();
        if (filename == null || !filename.toLowerCase().endsWith(".apk")) {
            throw new IllegalArgumentException("Invalid file type. APK file must end with .apk");
        }
    }

    public static void validateIpa(MultipartFile file) {
        if (file == null || file.isEmpty()) return;
        String filename = file.getOriginalFilename();
        if (filename == null || !filename.toLowerCase().endsWith(".ipa")) {
            throw new IllegalArgumentException("Invalid file type. IPA file must end with .ipa");
        }
    }

    public static void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) return;
        String filename = file.getOriginalFilename();
        if (filename == null) {
            throw new IllegalArgumentException("Invalid image file");
        }
        String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        if (!IMAGE_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("Invalid image type. Supported types: png, jpg, jpeg");
        }
    }
}
