package com.example.sprinkler.decision;

import java.time.Instant;
import java.util.Optional;
import java.util.function.Function;

import com.example.sprinkler.common.SprinklerEvent;
import com.example.sprinkler.common.SprinklerState;
import com.example.sprinkler.common.SprinklerStatus;
import com.example.sprinkler.common.SprinklerStatusRepository;
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
	public Function<SprinklerEvent, SprinklerEvent> timer(SprinklerStatusRepository statusRepository, SprinklerDecisionProperties properties) {
		return (input) -> {
			logger.info("timer:input:{}", input);
			Optional<SprinklerStatus> status = statusRepository.findLatest();
			if (status.isPresent() && status.get().getState().equals(SprinklerState.OFF)) {
				logger.info("timer:event:properties:{}", properties);
				Instant onTime = input.getTimestamp().toInstant().plus(properties.offTime);
				if (onTime.isAfter(status.get().getStatusTime().toInstant())) {
					var output = new SprinklerEvent(
						input.getId(),
						input.getTimestamp(),
						SprinklerState.ON,
						input.getReason() + ": timer " + properties.offTime.toString()
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
