package com.iexceed.appzillon.accounts.service;

import com.iexceed.appzillon.accounts.dto.CorporateResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.iexceed.appzillon.accounts.dto.ApiRequest;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import com.iexceed.appzillon.accounts.service.IntegrationAdapter;
import com.iexceed.appzillon.accounts.dto.ApiResponse;

@Service
public class CorporateService {

    private static final Logger logger = LoggerFactory.getLogger(CorporateService.class);

    private final RestTemplate restTemplate;

    @Autowired
    private IntegrationAdapter integrationAdapter;

    // Postman Mock URL (customerId is a path variable)
    private static final String MOCK_URL =
            "https://f1e02a7b-ebc2-4fa9-9aa8-ca5fc12935dc.mock.pstmn.io/corporate-details?customerId={customerId}";

    @Autowired
    public CorporateService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CorporateResponseDTO getCorporateDetails(String customerId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        logger.info("Calling Postman mock for customerId={}", customerId);

        ResponseEntity<CorporateResponseDTO> response = restTemplate.exchange(
                MOCK_URL,
                HttpMethod.GET,
                entity,
                CorporateResponseDTO.class,
                customerId
        );

        logger.info("Received response status: {}", response.getStatusCode());
        logger.info("Received response body: {}", response.getBody());
        CorporateResponseDTO dto = response.getBody();
        logger.debug("Mapped DTO: {}", dto);

        return dto;
    }

    // Added for dynamic API
    public Map<String, Object> getCorporateAccounts(ApiRequest request) {
        // Delegate to IntegrationAdapter for dynamic mapping
        String interfaceName = null;
        if (request != null && request.getRequestHeader() != null) {
            interfaceName = request.getRequestHeader().getInterfaceName();
        }
        if (interfaceName == null) interfaceName = "Customer360"; // fallback
        ApiResponse<Map<String, Object>> apiResponse = integrationAdapter.callExternalApi(interfaceName, request);
        return apiResponse != null ? apiResponse.getResponseBody() : null;
    }
}
