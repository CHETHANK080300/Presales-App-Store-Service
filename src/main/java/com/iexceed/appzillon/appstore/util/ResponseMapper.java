package com.iexceed.appzillon.appstore.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

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
                    // Extract value from JSON path e.g., ${RESPONSE~accounts.0.ACCOUNT_NO}
                    if (strVal.contains("${RESPONSE~")) {
                        String path = strVal.substring(strVal.indexOf("~") + 1, strVal.indexOf("}"));
                        String[] pathParts = path.split("\\.");
                        JsonNode temp = rootNode;
                        for (String p : pathParts) {
                            if (temp == null) break;
                            if (p.matches("\\d+")) {
                                // Array index
                                int idx = Integer.parseInt(p);
                                if (temp.isArray() && temp.size() > idx) {
                                    temp = temp.get(idx);
                                } else {
                                    temp = null;
                                    break;
                                }
                            } else {
                                if (temp.has(p)) temp = temp.get(p);
                                else { temp = null; break; }
                            }
                        }
                        mapped.put(key, temp != null && !temp.isNull() ? (temp.isValueNode() ? temp.asText() : temp) : null);
                    } else {
                        mapped.put(key, strVal); // literal value
                    }
                } else if (value instanceof Map) {
                    mapped.put(key, mapNode(rootNode, (Map<String, Object>) value));
                } else if (value instanceof List) {
                    // Support for mapping arrays of objects
                    List<?> defList = (List<?>) value;
                    if (!defList.isEmpty() && defList.get(0) instanceof Map) {
                        // Find the array node in the response by key
                        JsonNode arrayNode = rootNode.get(key);
                        List<Object> mappedList = new ArrayList<>();
                        if (arrayNode != null && arrayNode.isArray()) {
                            for (JsonNode itemNode : arrayNode) {
                                mappedList.add(mapNode(itemNode, (Map<String, Object>) defList.get(0)));
                            }
                        }
                        mapped.put(key, mappedList);
                    }
                }
            } catch (Exception e) {
                mapped.put(key, null);
            }
        });
        return mapped;
    }
}
