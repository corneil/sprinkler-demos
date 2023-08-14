package com.example.sprinkler.sprinkler_data_consumer;

import java.util.function.Consumer;

import com.example.sprinkler.common.SimulationService;
import com.example.sprinkler.common.SprinklerEvent;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.integration.config.EnableIntegration;

@Configuration
@EnableIntegration
public class SprinklerDataConsumerConfig {
    @Bean(name = "data")
    @Primary
    public Consumer<SprinklerEvent> sprinklerDataConsumer(SimulationService service) {

        return service::updateSprinkler;
    }
}
