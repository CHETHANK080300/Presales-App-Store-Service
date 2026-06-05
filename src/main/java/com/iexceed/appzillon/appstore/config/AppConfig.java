package com.iexceed.appzillon.appstore.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class AppConfig {

    @Value("${app.upload.base-path}")
    private String uploadBasePath;

    @Value("${app.download.context-path}")
    private String downloadContextPath;

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOriginPatterns("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }

            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                String location = "file:" + uploadBasePath;
                if (!location.endsWith("/")) {
                    location += "/";
                }
                registry.addResourceHandler(downloadContextPath + "/**")
                        .addResourceLocations(location);
            }
        };
    }
}