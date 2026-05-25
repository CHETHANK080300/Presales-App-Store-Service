package com.iexceed.appzillon.appstore.service;

import com.iexceed.appzillon.appstore.dto.request.AppMasterRequestDto;
import com.iexceed.appzillon.appstore.dto.response.AppMasterResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface AppMasterService {

    AppMasterResponseDto uploadApplication(
            AppMasterRequestDto requestDto,
            MultipartFile apkFile,
            MultipartFile ipaFile,
            MultipartFile plistFile,
            MultipartFile imageFile
    );
}