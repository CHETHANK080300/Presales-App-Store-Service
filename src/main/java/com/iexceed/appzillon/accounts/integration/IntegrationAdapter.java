package com.iexceed.appzillon.accounts.integration;

import com.iexceed.appzillon.accounts.utils.MapUtils;
import java.util.*;
import java.util.regex.*;

public class IntegrationAdapter {
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{(REQUEST|RESPONSE)~([\\w.]+)\\}");

    public static Map<String, Object> buildRequest(Map<String, Object> requestDef, Map<String, Object> requestHeader, Map<String, Object> requestBody) {
        return resolveMap(requestDef, requestHeader, requestBody, null, "REQUEST");
    }

    public static Map<String, Object> buildResponse(Map<String, Object> responseDef, Map<String, Object> responseMap) {
        return resolveMap(responseDef, null, null, responseMap, "RESPONSE");
    }

    private static Map<String, Object> resolveMap(Map<String, Object> def, Map<String, Object> requestHeader, Map<String, Object> requestBody, Map<String, Object> responseMap, String mode) {
        Map<String, Object> result = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : def.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof String) {
                result.put(entry.getKey(), resolvePlaceholder((String) value, requestHeader, requestBody, responseMap, mode));
            } else if (value instanceof Map) {
                result.put(entry.getKey(), resolveMap((Map<String, Object>) value, requestHeader, requestBody, responseMap, mode));
            } else {
                result.put(entry.getKey(), value);
            }
        }
        return result;
    }

    private static Object resolvePlaceholder(String value, Map<String, Object> requestHeader, Map<String, Object> requestBody, Map<String, Object> responseMap, String mode) {
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(value);
        if (matcher.matches()) {
            String type = matcher.group(1);
            String path = matcher.group(2);
            if ("REQUEST".equals(type) && "REQUEST".equals(mode)) {
                return getValueByPath(path, requestHeader, requestBody);
            } else if ("RESPONSE".equals(type) && "RESPONSE".equals(mode)) {
                return getValueByPath(path, responseMap, null);
            }
        }
        return value;
    }

    private static Object getValueByPath(String path, Map<String, Object> primary, Map<String, Object> secondary) {
        String[] parts = path.split("\\.");
        Object current = null;
        if (primary != null && primary.containsKey(parts[0])) {
            current = primary.get(parts[0]);
        } else if (secondary != null && secondary.containsKey(parts[0])) {
            current = secondary.get(parts[0]);
        }
        for (int i = 1; i < parts.length; i++) {
            if (current instanceof Map) {
                current = ((Map<?, ?>) current).get(parts[i]);
            } else {
                return null;
            }
        }
        return current;
    }
}
