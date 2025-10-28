package com.BiblioStock.BiblioStock_API.config; 

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("BiblioStock API") 
                .version("1.0") 
                .description("Esta API é responsável por todas as operações de CRUD do sistema BiblioStock.")
            );
    }
}