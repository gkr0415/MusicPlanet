package com.music.music_inventory_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI configuration for API documentation. Access Swagger UI at:
 * http://localhost:8080/swagger-ui.html Access API docs at:
 * http://localhost:8080/api-docs
 */
@Configuration
public class SwaggerConfig
{

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public OpenAPI customOpenAPI()
    {
        return new OpenAPI().info(new Info().title(applicationName).version("1.0.0").description(
                "A comprehensive music store REST API for managing artists, albums, songs, genres, customers, and orders. "
                        + "This API provides full CRUD operations and advanced search capabilities.")
                .contact(new Contact().name("Music Store Team").email("support@musicstore.com"))
                .license(new License().name("MIT License").url("https://opensource.org/licenses/MIT")))
                .servers(List.of(new Server().url("http://localhost:8080").description("Development Server"),
                        new Server().url("https://api.musicstore.com").description("Production Server")));
    }
}
