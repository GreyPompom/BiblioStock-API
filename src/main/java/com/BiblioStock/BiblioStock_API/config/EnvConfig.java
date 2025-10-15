package com.BiblioStock.BiblioStock_API.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvConfig {

    @Bean
    public Dotenv dotenv() {
        return Dotenv.configure()
                     .directory(".")
                     .ignoreIfMissing()  // evita erro se .env não existir
                     .load();
    }

}