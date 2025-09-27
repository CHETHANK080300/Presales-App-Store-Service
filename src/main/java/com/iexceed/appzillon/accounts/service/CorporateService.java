package com.iexceed.appzillon.accounts.service;

import com.iexceed.appzillon.accounts.dto.CorporateResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CorporateService {

    private static final Logger logger = LoggerFactory.getLogger(CorporateService.class);

    private final RestTemplate restTemplate;

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
}
