package com.librarysystem.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Sistema de Gestión de Biblioteca")
                        .version("1.0")
                        .description("Documentación de los endpoints para gestionar libros, usuarios y préstamos.")
                        .contact(new Contact()
                                .name("Soporte Biblioteca")
                                .email("soporte@biblioteca.com")));
    }
}



