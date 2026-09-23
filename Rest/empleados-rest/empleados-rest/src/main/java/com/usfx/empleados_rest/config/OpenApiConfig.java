package com.usfx.empleados_rest.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI empleadosOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("API REST de Empleados")
                        .description(
                                "Ejemplo de API REST utilizando Spring Boot, MySQL y Swagger"
                        )
                        .version("1.0"));
    }
}
