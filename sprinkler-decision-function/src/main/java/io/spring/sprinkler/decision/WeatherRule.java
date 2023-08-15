package io.spring.sprinkler.decision;

import java.util.Optional;
import java.util.function.Function;

import io.spring.sprinkler.common.SimulationService;
import io.spring.sprinkler.common.SprinklerEvent;
import io.spring.sprinkler.common.SprinklerState;
import io.spring.sprinkler.common.WeatherData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SprinklerDecisionProperties.class)
public class WeatherRule {
	private final static Logger logger = LoggerFactory.getLogger(WeatherRule.class);

	@Bean(name = "weather")
	public Function<SprinklerEvent, SprinklerEvent> weather(SimulationService simulationService, SprinklerDecisionProperties properties) {
		return (input) -> {
			logger.info("input:{}", input);
			if (input != null) {
				if (input.getState().equals(SprinklerState.ON)) {
					Double minPrediction = properties.getMinPrediction();
					logger.info("minPrediction:{}", minPrediction);
					Optional<WeatherData> weatherData = simulationService.latestWeather(input.getTimestamp());
					if (weatherData.isPresent()) {
						logger.info("weather:{}", weatherData.get());
						if (minPrediction != null) {
							Double prediction = weatherData.get().getPrediction();
							if (prediction >= minPrediction) {
								SprinklerEvent result = new SprinklerEvent(input.getId(),
									input.getTimestamp(),
									SprinklerState.OFF,
									input.getReason() + ":OFF:prediction=" + prediction + ":minPrediction=" + minPrediction);
								logger.info("weather:result={}", result);
								return result;
							}
						}
					}
				}
			}
			return input;
		};
	}
}
