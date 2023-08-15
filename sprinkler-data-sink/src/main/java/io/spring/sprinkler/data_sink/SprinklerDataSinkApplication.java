package io.spring.sprinkler.data_sink;

import io.spring.sprinkler.client.SimulationClientProperties;
import io.spring.sprinkler.client.SprinklerClientConfig;
import io.spring.sprinkler.data_consumer.SprinklerDataConsumerConfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableConfigurationProperties(SimulationClientProperties.class)
@Import({SprinklerClientConfig.class, SprinklerDataConsumerConfig.class})
public class SprinklerDataSinkApplication {
    public static void main(String[] args) {
        SpringApplication.run(SprinklerDataSinkApplication.class, args);
    }
}
