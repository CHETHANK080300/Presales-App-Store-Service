package com.iexceed.appzillon.accounts.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Map;

@Service
public class InterfaceSpecLoader {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, Object> loadInterfaceSpec(String interfaceName) throws Exception {
        // Load JSON file from resources/interface/{interfaceName}.json
        File file = new File("src/main/resources/interface/" + interfaceName + ".json");
        return objectMapper.readValue(file, Map.class);
    }
}
