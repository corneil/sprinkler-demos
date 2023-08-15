package io.spring.sprinkler.decision;

import java.util.function.Function;

import io.spring.sprinkler.common.DateRange;
import io.spring.sprinkler.common.SimulationService;
import io.spring.sprinkler.common.SprinklerEvent;
import io.spring.sprinkler.common.SprinklerState;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jackson.JsonComponentModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

@Configuration
@EnableConfigurationProperties(SprinklerDecisionProperties.class)
public class RainRule {
	private final static Logger logger = LoggerFactory.getLogger(RainRule.class);


	@Bean(name = "rain")
	public Function<SprinklerEvent, SprinklerEvent> rain(SimulationService simulationService, SprinklerDecisionProperties properties) {
		return (input) -> {
			logger.info("input:{}", input);
			if (input.getState().equals(SprinklerState.OFF)) {
				Double minRainMeasure = properties.getMinRainMeasure();
				logger.info("minRainMeasure={}", minRainMeasure);
				DateRange dateRange = new DateRange(input.getTimestamp().minus(properties.cycleDuration), input.getTimestamp());
				Double rainMeasured = simulationService.rainMeasuredFor(dateRange);
				logger.info("rainMeasured:{} for {}", rainMeasured, dateRange);
				if (rainMeasured >= minRainMeasure) {
					SprinklerEvent result = new SprinklerEvent(input.getId(),
						input.getTimestamp(),
						SprinklerState.OFF,
						input.getReason() + ":OFF:rainMeasured=" + rainMeasured + ":minRainMeasure=" + minRainMeasure);
					logger.info("result={}", result);
					return result;
				}
			}
			return input;
		};
	}

}
