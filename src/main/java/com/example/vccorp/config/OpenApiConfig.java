package com.example.vccorp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.url}")
    private String serverUrl;

    @Bean
    public OpenAPI idolOpenAPI() {
        Server urlServer = new Server()
                .url(serverUrl);

        return new OpenAPI()
                .info(new Info()
                        .title("Basic swagger Adopt")
                        .version("1.0.0")
                        .description("API documentation"))
                .servers(List.of(urlServer));
    }
} 