package com.iexceed.appzillon.appstore.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppMasterResponseDto {

    private String status;
    private String message;
    private String appId;
}