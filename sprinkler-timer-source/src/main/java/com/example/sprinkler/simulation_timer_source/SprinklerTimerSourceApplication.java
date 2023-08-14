package com.example.sprinkler.simulation_timer_source;

import com.example.sprinkler.client.SimulationClientProperties;
import com.example.sprinkler.client.SprinklerClientConfig;
import com.example.sprinkler.simulation_timer.SimulationTimerConfiguration;
import com.example.sprinkler.simulation_timer.TimerSimulationProperties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.function.context.FunctionProperties;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({SimulationTimerConfiguration.class, SprinklerClientConfig.class, FunctionProperties.class})
@EnableConfigurationProperties({SimulationClientProperties.class, TimerSimulationProperties.class})
public class SprinklerTimerSourceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SprinklerTimerSourceApplication.class, args);
    }
}
