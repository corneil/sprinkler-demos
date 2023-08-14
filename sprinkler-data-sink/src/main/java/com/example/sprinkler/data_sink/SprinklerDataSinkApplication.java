package com.example.sprinkler.data_sink;

import com.example.sprinkler.client.SimulationClientProperties;
import com.example.sprinkler.client.SprinklerClientConfig;
import com.example.sprinkler.sprinkler_data_consumer.SprinklerDataConsumerConfig;

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
