package com.example.sprinkler.decision;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.function.Function;

import com.example.sprinkler.common.SimulationService;
import com.example.sprinkler.common.SprinklerEvent;
import com.example.sprinkler.common.SprinklerState;
import com.example.sprinkler.common.SprinklerStatus;
import com.example.sprinkler.common.service.SprinklerStatusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SprinklerDecisionProperties.class)
public class TimerRule {
	private final static Logger logger = LoggerFactory.getLogger(TimerRule.class);

	@Bean
	public Function<SprinklerEvent, SprinklerEvent> timer(SimulationService service, SprinklerDecisionProperties properties) {
		return (input) -> {
			logger.info("timer:input:{}", input);
			Optional<SprinklerStatus> status = service.findLatestStatus(input.timestamp());
			if (status.isPresent() && status.get().state().equals(SprinklerState.OFF)) {
				logger.info("timer:event:properties:{}", properties);
				ZonedDateTime onTime = input.timestamp().plus(properties.offTime);
				if (onTime.isAfter(status.get().statusTime())) {
					var output = new SprinklerEvent(
						input.id(),
						input.timestamp(),
						SprinklerState.ON,
						input.reason() + ": timer " + properties.offTime.toString()
					);
					logger.info("timer:output:{}", output);
					return output;
				}
			}
			logger.info("timer:output:null");
			return null;
		};
	}
}
