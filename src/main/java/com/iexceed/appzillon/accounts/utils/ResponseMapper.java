package com.iexceed.appzillon.accounts.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResponseMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Maps external API JSON to response structure based on responseDef
     *
     * @param responseJson external API JSON string
     * @param responseDef  mapping definition from interface JSON
     * @return mapped response as Map
     * @throws Exception
     */
    public static Map<String, Object> mapResponse(String responseJson, Map<String, Object> responseDef) throws Exception {
        JsonNode rootNode = objectMapper.readTree(responseJson);
        return mapNode(rootNode, responseDef);
    }

    private static Map<String, Object> mapNode(JsonNode rootNode, Map<String, Object> responseDef) {
        Map<String, Object> mapped = new LinkedHashMap<>();
        responseDef.forEach((key, value) -> {
            try {
                if (value instanceof String) {
                    String strVal = (String) value;
                    // Extract value from JSON path e.g., ${RESPONSE~accounts.accountNo}
                    if (strVal.contains("${RESPONSE~")) {
                        String path = strVal.substring(strVal.indexOf("~") + 1, strVal.indexOf("}"));
                        String[] pathParts = path.split("\\.");
                        JsonNode temp = rootNode;
                        for (String p : pathParts) {
                            if (temp.has(p)) temp = temp.get(p);
                            else { temp = null; break; }
                        }
                        mapped.put(key, temp != null && !temp.isNull() ? temp.asText() : null);
                    } else {
                        mapped.put(key, strVal); // literal value
                    }
                } else if (value instanceof Map) {
                    mapped.put(key, mapNode(rootNode, (Map<String, Object>) value));
                }
            } catch (Exception e) {
                mapped.put(key, null);
            }
        });
        return mapped;
    }
}
