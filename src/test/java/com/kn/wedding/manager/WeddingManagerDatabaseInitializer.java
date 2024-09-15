package com.kn.wedding.manager;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.startupcheck.MinimumDurationRunningStartupCheckStrategy;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

@Component
@Testcontainers
public class WeddingManagerDatabaseInitializer {

    @Bean
    @ServiceConnection
    public PostgreSQLContainer<?> connection() {
        return new PostgreSQLContainer<>(DockerImageName.parse("postgres:alpine")).withInitScript("init.sql")
                        .withReuse(true)
                        .withExposedPorts(5432)
                        .withStartupCheckStrategy(new MinimumDurationRunningStartupCheckStrategy(Duration.ofSeconds(5)));
    }
}