package com.iexceed.appzillon.accounts.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Map;

@Service
public class InterfaceSpecLoader {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${external.interface.json.path}")
    private String interfaceJsonPath;

    public Map<String, Object> loadInterfaceSpec(String interfaceName) throws Exception {
        // Load JSON file from external path specified in properties
        File file = new File(interfaceJsonPath, interfaceName + ".json");
        return objectMapper.readValue(file, Map.class);
    }
}
