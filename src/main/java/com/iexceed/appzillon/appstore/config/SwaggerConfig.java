package com.iexceed.appzillon.appstore.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI corporateApiOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Presales App Store Service API")
                        .description("API's of Presales App Store")
                        .version("1.0.0"));
    }
}