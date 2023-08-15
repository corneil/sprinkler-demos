package io.spring.sprinkler.data_consumer;

import java.util.function.Consumer;

import io.spring.sprinkler.common.SimulationService;
import io.spring.sprinkler.common.SprinklerEvent;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class SprinklerDataConsumerConfig {
	@Bean(name = "data")
	@Primary
	public Consumer<SprinklerEvent> data(SimulationService service) {

		return service::updateSprinkler;
	}
}
