package com.kn.wedding.manager;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration(proxyBeanMethods = false)
@OpenAPIDefinition(
        info = @Info(
                title = "${api.description}",
                version = "${api.version}",
                contact = @Contact(name = "Kevin Navarro", email = "ken314588@gmail.com"),
                description = "${api.description}"
        )
)
class OpenApiConfig implements WebMvcConfigurer {
    private final List<String> ALLOWED_ORIGINS = List.of("http://localhost:8000", "https://boda-mk-be.com", "https://boda-mk.com");

//    @Bean
//    public OpenAPI customOpenAPI() {
//        return new OpenAPI()
//                .addServersItem(new Server().url("http://localhost:8484/api").description("HTTPS Server"));
//    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(ALLOWED_ORIGINS.toArray(new String[0]))
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}