package com.springboot.coding.testingApp;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class TestContainerConfiguration {

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgresContainer() {

        String image = "postgres:latest";
//        String image = "postgres:16";
        return new PostgreSQLContainer<>(DockerImageName.parse(image));
    }
}
