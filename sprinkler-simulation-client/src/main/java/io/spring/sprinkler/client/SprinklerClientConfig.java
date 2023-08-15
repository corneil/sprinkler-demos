package io.spring.sprinkler.client;

import io.spring.sprinkler.common.SimulationService;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

@Configuration
@Profile("simulation-client")
@EnableConfigurationProperties(SimulationClientProperties.class)
public class SprinklerClientConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public SimulationService simulationClient(SimulationClientProperties properties) {
        return new SimulationClient(properties.getServerApiUrl(), restTemplate());
    }

}
