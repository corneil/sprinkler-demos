package io.spring.sprinkler.data_consumer;

import java.util.function.Consumer;

import io.spring.sprinkler.common.SimulationService;
import io.spring.sprinkler.common.SprinklerEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class SprinklerDataConsumerConfig {
    private final static Logger logger = LoggerFactory.getLogger("data");
    @Bean
    @Primary
    public Consumer<SprinklerEvent> data(SimulationService service) {
        return event -> {
            logger.info("event:{}", event);
            if (event != null) {
                service.updateSprinkler(event);
            }
        };
    }
}
