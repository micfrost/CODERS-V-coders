package com.dci.a3m.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class GlobalCorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {

                // Configure CORS for allowing requests from http://localhost:3000

                registry.addMapping("/api/**")  // Mapping for all endpoints
                        .allowedOrigins("http://localhost:3000")  // Allowing requests from localhost:3000
                        .allowedMethods("GET", "POST", "PUT", "DELETE")  // Allowing specified HTTP methods
                        .allowedHeaders("*")  // Allowing all headers
                        .allowCredentials(true);  // Allowing credentials like cookies, authorization headers, etc.
            }
        };
    }


}
