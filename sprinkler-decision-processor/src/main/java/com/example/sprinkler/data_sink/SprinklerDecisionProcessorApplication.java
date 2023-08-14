package com.example.sprinkler.data_sink;

import com.example.sprinkler.client.SprinklerClientConfig;
import com.example.sprinkler.decision.SprinklerDecisionProperties;
import com.example.sprinkler.decision.TimerRule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.integration.config.EnableIntegration;

@SpringBootApplication
@EnableIntegration
@EnableConfigurationProperties({SprinklerDecisionProperties.class})
@Import({SprinklerClientConfig.class, TimerRule.class})
public class SprinklerDecisionProcessorApplication {
    public static void main(String[] args) {
        SpringApplication.run(SprinklerDecisionProcessorApplication.class, args);
    }
}
