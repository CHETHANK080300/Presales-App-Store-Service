package com.iexceed.appzillon.accounts.service;

import com.iexceed.appzillon.accounts.dto.ApiRequest;
import com.iexceed.appzillon.accounts.dto.ApiResponse;
import com.iexceed.appzillon.accounts.dto.ResponseHeader;
import com.iexceed.appzillon.accounts.utils.ResponseMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class IntegrationAdapter {

    @Autowired
    private InterfaceSpecLoader specLoader;

    @Autowired
    private RequestMapper requestMapper;

    @Autowired
    private RestTemplate restTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ApiResponse<Map<String, Object>> callExternalApi(String interfaceName, ApiRequest<?> apiRequest) {
        try {
            // 1. Load interface JSON spec
            Map<String, Object> spec = specLoader.loadInterfaceSpec(interfaceName);

            // 2. Build request dynamically
            Map<String, Object> requestDef = (Map<String, Object>) spec.get("requestDef");
            Map<String, Object> requestBody = requestMapper.buildRequest(apiRequest, requestDef);

            // 3. Build HTTP headers
            Map<String, String> headersMap = (Map<String, String>) spec.get("headers");
            HttpHeaders headers = new HttpHeaders();
            headers.setAll(headersMap);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // 4. Call external API
            String url = (String) spec.get("apiURL");
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.valueOf(((String) spec.get("methodType")).toUpperCase()),
                    entity,
                    String.class
            );

            String externalResponse = responseEntity.getBody();

            // 5. Map external response dynamically according to responseDef
            Map<String, Object> responseDef = (Map<String, Object>) spec.get("responseDef");
            Map<String, Object> mappedResponse = ResponseMapper.mapResponse(externalResponse, responseDef);

            // 6. Return wrapped API response
            return new ApiResponse<>(
                    0,
                    new ResponseHeader("200", "Corporate Data fetched successfully"),
                    mappedResponse
            );

        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>(
                    1,
                    new ResponseHeader("500", "Failed to fetch data: " + e.getMessage()),
                    null
            );
        }
    }
}
