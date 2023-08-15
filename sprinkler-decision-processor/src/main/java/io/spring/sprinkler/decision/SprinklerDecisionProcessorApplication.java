package io.spring.sprinkler.decision;

import io.spring.sprinkler.client.SprinklerClientConfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableConfigurationProperties({SprinklerDecisionProperties.class})
@Import({SprinklerClientConfig.class, TimerRule.class})
public class SprinklerDecisionProcessorApplication {
	public static void main(String[] args) {
		SpringApplication.run(SprinklerDecisionProcessorApplication.class, args);
	}
}
