package com.aryunin.conveyor.configuration;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    @Value("${project.version}")
    private String version;
    @Bean
    public OpenAPI myOpenAPI() {

        Info info = new Info()
                .title("Credit conveyor API")
                .version(version);

        return new OpenAPI().info(info);
    }
}