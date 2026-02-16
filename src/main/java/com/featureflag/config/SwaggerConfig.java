package com.featureflag.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Feature Flag System API")
                        .version("1.0.0")
                        .description("Production-grade feature flag management system")
                        .contact(new Contact()
                                .name("Support")
                                .email("support@featureflag.com")))
                .addServersItem(new Server()
                        .url("http://localhost:8080")
                        .description("Development"));
    }
}
