package com.example.sprinkler.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;

import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestSprinklerSimulationAppApplication {

    @Bean
    @ServiceConnection
    MariaDBContainer<?> mariaDbContainer() {
        return new MariaDBContainer<>(DockerImageName.parse("mariadb:latest"));
    }

    public static void main(String[] args) {
        SpringApplication.from(SprinklerSimulationAppApplication::main).with(TestSprinklerSimulationAppApplication.class).run(args);
    }

}
