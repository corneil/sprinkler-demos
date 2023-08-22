package io.spring.sprinkler.app;

import io.spring.sprinkler.common.service.SprinklerServiceConfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(SprinklerServiceConfig.class)
public class SprinklerSimulationApplication {

    public static void main(String[] args) {
        SpringApplication.run(SprinklerSimulationApplication.class, args);
    }

}
