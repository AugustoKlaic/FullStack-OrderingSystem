package com.augusto.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI springOpenAPI() {
        final String securitySchemeName = "JWT Bearer Auth";
        return new OpenAPI().components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("Bearer")
                                .bearerFormat("JWT")))
                .security(List.of(new SecurityRequirement().addList(securitySchemeName)))
                .info(new Info()
                        .title("Fullstack-OrderingSystem")
                        .version("1.0")
                        .description("Backend APIs")
                        .contact(new Contact()
                                .name("Augusto Sopelsa")
                                .url("https://github.com/AugustoKlaic/FullStack-OrderingSystem"))
                        .termsOfService("https://www.udemy.com/terms")
                        .license(new License().name("For studies only")));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public-apis")
                .packagesToScan("com.augusto.backend.resource")
                .pathsToMatch("/**")
                .build();
    }

}
