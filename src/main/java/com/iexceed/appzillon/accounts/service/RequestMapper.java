package com.iexceed.appzillon.accounts.service;

import com.iexceed.appzillon.accounts.dto.ApiRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RequestMapper {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{REQUEST~(.+?)\\}");

    public Map<String, Object> buildRequest(ApiRequest<?> apiRequest, Map<String, Object> requestDef) {
        Map<String, Object> finalRequest = new HashMap<>();

        // Convert apiRequest to JsonNode for easy path lookup
        JsonNode requestNode = objectMapper.valueToTree(apiRequest);

        requestDef.forEach((key, value) -> {
            if (value instanceof String) {
                String strVal = (String) value;

                Matcher matcher = PLACEHOLDER_PATTERN.matcher(strVal);
                StringBuffer sb = new StringBuffer();

                while (matcher.find()) {
                    String path = matcher.group(1);
                    JsonNode node = requestNode.at("/" + path.replace(".", "/"));
                    String replacement = node.isMissingNode() ? "" : node.asText();
                    matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
                }
                matcher.appendTail(sb);

                finalRequest.put(key, sb.toString());
            } else {
                finalRequest.put(key, value);
            }
        });

        return finalRequest;
    }
}
