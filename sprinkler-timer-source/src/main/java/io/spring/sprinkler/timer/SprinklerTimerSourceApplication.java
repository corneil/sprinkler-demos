package io.spring.sprinkler.timer;

import io.spring.sprinkler.client.SimulationClientProperties;
import io.spring.sprinkler.client.SprinklerClientConfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({SimulationTimerConfiguration.class, SprinklerClientConfig.class})
@EnableConfigurationProperties({SimulationClientProperties.class, TimerSimulationProperties.class})
public class SprinklerTimerSourceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SprinklerTimerSourceApplication.class, args);
    }
}
