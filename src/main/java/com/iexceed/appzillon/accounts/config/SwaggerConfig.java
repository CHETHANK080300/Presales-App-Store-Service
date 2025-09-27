package com.iexceed.appzillon.accounts.config;

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
                        .title("Corporate Accounts Service API")
                        .description("API to fetch corporate details and list of accounts")
                        .version("1.0.0"));
    }
}