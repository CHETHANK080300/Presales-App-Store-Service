package com.iexceed.appzillon.accounts.service;

import com.iexceed.appzillon.accounts.dto.ApiRequest;
import com.iexceed.appzillon.accounts.dto.ApiResponse;
import com.iexceed.appzillon.accounts.dto.ResponseHeader;
import com.iexceed.appzillon.accounts.utils.ResponseMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iexceed.appzillon.accounts.hooks.PreHookProcessor;
import com.iexceed.appzillon.accounts.hooks.PostHookProcessor;
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

    @Autowired
    private PreHookProcessor preHookProcessor;

    @Autowired
    private PostHookProcessor postHookProcessor;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ApiResponse<Map<String, Object>> callExternalApi(String interfaceName, ApiRequest<?> apiRequest) {
        try {
            // PRE HOOK: manipulate/validate/augment the request
            apiRequest = preHookProcessor.process(apiRequest, interfaceName);

            // 1. Load interface JSON spec
            Map<String, Object> spec = specLoader.loadInterfaceSpec(interfaceName);

            // 2. Build request dynamically
            Map<String, Object> requestDef = (Map<String, Object>) spec.get("requestDef");
            Map<String, Object> requestBody = requestMapper.buildRequest(apiRequest, requestDef);

            // 3. Build HTTP headers
            Map<String, String> headersMap = (Map<String, String>) spec.get("headers");
            HttpHeaders headers = new HttpHeaders();
            if (headersMap != null) {
                headers.setAll(headersMap);
            }
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // 4. Call external API
            String url = (String) spec.get("apiURL");

            String methodType = (String) spec.get("methodType");
            if (methodType == null || methodType.isEmpty()) {
                return new ApiResponse<>(
                        1,
                        new ResponseHeader("405", "No HTTP methodType defined in interface spec"),
                        null
                );
            }
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.valueOf(methodType.toUpperCase()),
                    entity,
                    String.class
            );

            String externalResponse = responseEntity.getBody();

            // 5. Map external response dynamically according to responseDef
            Map<String, Object> responseDef = (Map<String, Object>) spec.get("responseDef");
            Map<String, Object> mappedResponse = ResponseMapper.mapResponse(externalResponse, responseDef);

            // POST HOOK: manipulate/validate/augment the mapped response
            mappedResponse = postHookProcessor.process(mappedResponse, interfaceName);

            // 6. Return wrapped API response
            String statusMessage;
            if (interfaceName != null && (interfaceName.toLowerCase().contains("fundtransfernapas") || interfaceName.toLowerCase().contains("fundtransfercitad"))) {
                statusMessage = "Fund Transfer processed successfully";
            } else {
                statusMessage = "Corporate Data fetched successfully";
            }
            return new ApiResponse<>(
                    0,
                    new ResponseHeader("200", statusMessage),
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
