package com.iexceed.appzillon.accounts.service;

import com.iexceed.appzillon.accounts.dto.ApiRequest;
import com.iexceed.appzillon.accounts.dto.ApiResponse;
import com.iexceed.appzillon.accounts.service.IntegrationAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FundTransferService {

    @Autowired
    private IntegrationAdapter integrationAdapter;

    @Value("${external.interface.json.path}")
    private String interfaceJsonPath;

    public ApiResponse processFundTransfer(ApiRequest<?> request) {
        Object body = request.getRequestBody();
        String transferType = null;
        if (body instanceof com.iexceed.appzillon.accounts.dto.FundTransferRequestBody) {
            transferType = ((com.iexceed.appzillon.accounts.dto.FundTransferRequestBody) body).getTransferType();
        }
        String interfaceFile;
        if ("NAPAS".equalsIgnoreCase(transferType)) {
            interfaceFile = "FundTransferNAPAS.json";
        } else if ("CITAD".equalsIgnoreCase(transferType)) {
            interfaceFile = "FundTransferCITAD.json";
        } else {
            throw new IllegalArgumentException("Unsupported transferType: " + transferType);
        }
        return integrationAdapter.callExternalApi(interfaceFile, request);
    }
}
