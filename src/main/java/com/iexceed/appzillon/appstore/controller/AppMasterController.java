package com.iexceed.appzillon.appstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iexceed.appzillon.appstore.dto.request.AppMasterRequestDto;
import com.iexceed.appzillon.appstore.dto.response.AppMasterResponseDto;
import com.iexceed.appzillon.appstore.service.AppMasterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/apps")
@RequiredArgsConstructor
public class AppMasterController {

    private final AppMasterService service;
    private final ObjectMapper objectMapper;

    @Operation(summary = "Upload Mobile Application")
    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<AppMasterResponseDto> uploadApplication(

            @RequestPart("request")
            String requestJson,

            @RequestPart(value = "apkFile", required = false)
            MultipartFile apkFile,

            @RequestPart(value = "ipaFile", required = false)
            MultipartFile ipaFile,

            @RequestPart(value = "plistFile", required = false)
            MultipartFile plistFile,

            @RequestPart(value = "imageFile", required = false)
            MultipartFile imageFile
    ) throws Exception {

        AppMasterRequestDto requestDto =
                objectMapper.readValue(
                        requestJson,
                        AppMasterRequestDto.class
                );

        AppMasterResponseDto response =
                service.uploadApplication(
                        requestDto,
                        apkFile,
                        ipaFile,
                        plistFile,
                        imageFile
                );

        return ResponseEntity.ok(response);
    }
}