package com.iexceed.appzillon.accounts.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.iexceed.appzillon.accounts.dto.ApiRequest;
import com.iexceed.appzillon.accounts.dto.ApiResponse;
import com.iexceed.appzillon.accounts.dto.CorporateResponseDTO;
import com.iexceed.appzillon.accounts.dto.ResponseHeader;
import com.iexceed.appzillon.accounts.service.CorporateService;
import com.iexceed.appzillon.accounts.service.IntegrationAdapter;

@RestController
@RequestMapping("/api/v1/corporate")
@Tag(name = "Corporate Accounts API", description = "API to fetch corporate details and accounts")
public class CorporateController {

    private final CorporateService corporateService;

    @Autowired
    public CorporateController(CorporateService corporateService) {
        this.corporateService = corporateService;
    }
    
    @Autowired
    private IntegrationAdapter integrationAdapter;

    @PostMapping("/corporate/accounts")
    public ApiResponse getCorporateAccounts(@RequestBody ApiRequest request) {
        Map<String, Object> responseMap = corporateService.getCorporateAccounts(request);
        ApiResponse apiResponse = new ApiResponse();
        if (responseMap != null) {
            apiResponse.setServiceStatusCode(0);
            apiResponse.setResponseHeader(new ResponseHeader("0", "Corporate data fetched successfully"));
            apiResponse.setResponseBody(responseMap);
        } else {
            apiResponse.setServiceStatusCode(1);
            apiResponse.setResponseHeader(new ResponseHeader("1", "No data returned from integration"));
            apiResponse.setResponseBody(null);
        }
        return apiResponse;
    }

    @GetMapping("/details")
    @Operation(summary = "Get corporate details and accounts by customerId")
    public CorporateResponseDTO getCorporateDetails(@RequestParam String customerId) {
        return corporateService.getCorporateDetails(customerId);
    }
}