package com.example.sprinkler.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestSprinklerSimulationAppApplication {

    static final MariaDBContainer<?> container = new MariaDBContainer<>(DockerImageName.parse("mariadb:latest"));

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.driver-class-name", container::getDriverClassName);
    }
    public static void main(String[] args) {
        SpringApplication.from(SprinklerSimulationAppApplication::main).with(TestSprinklerSimulationAppApplication.class).run(args);
    }

}
