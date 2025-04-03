package com.it.config;

import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("Jwt Api Access key"))
                .components(new Components().addSecuritySchemes("Jwt Api Access key", new SecurityScheme()
                        .name("Jwt Api Access key").type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));

    }

}
