package com.example.sprinkler.decision;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.function.Function;

import com.example.sprinkler.common.SimulationService;
import com.example.sprinkler.common.SprinklerEvent;
import com.example.sprinkler.common.SprinklerState;
import com.example.sprinkler.common.SprinklerStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
@EnableConfigurationProperties(SprinklerDecisionProperties.class)
public class TimerRule {
	private final static Logger logger = LoggerFactory.getLogger(TimerRule.class);

	@Bean(name = "timer")
	public Function<SprinklerEvent, SprinklerEvent> timer(SimulationService service, SprinklerDecisionProperties properties) {
		return input -> {
			logger.info("timer:input:{}", input);
			logger.info("timer:properties={}", properties);
			String reason = null;
			SprinklerState state = null;
			final ZonedDateTime now = input.getTimestamp();
			Optional<SprinklerStatus> status = service.findLatestStatus(now);
			if (status.isPresent()) {
				logger.info("timer:status={}", status.get());
				if (SprinklerState.OFF.equals(status.get().getState())) {
					// turn on when the event time + cycleTime is before now
					ZonedDateTime onTime = status.get().getStatusTime().plus(properties.cycleDuration);
					if (onTime.isBefore(now)) {
						state = SprinklerState.ON;
						reason = (input.getReason() != null ? input.getReason() : "");
						if (StringUtils.hasLength(reason)) {
							reason += ":";
						}
						reason += "ON:cycleTime=" + properties.cycleDuration + ", onTime=" + onTime + ":lastEventTime=" + status.get().getStatusTime();
					}
				} else {
					// turn off when the event time + onDuration is before now
					ZonedDateTime offTime = now.plus(properties.onDuration);
					if (offTime.isBefore(now)) {
						state = SprinklerState.OFF;
						reason = (input.getReason() != null ? input.getReason() : "");
						if (StringUtils.hasLength(reason)) {
							reason += ":";
						}
						reason += "OFF:onTime=" + properties.onDuration + ":lastEventTime=" + status.get().getStatusTime();
					}
				}
			} else {
				state = SprinklerState.OFF;
				reason = "OFF:No state";
			}
			if (state != null) {
				SprinklerEvent output = new SprinklerEvent(input.getId(), now, state, reason);
				logger.info("timer:output:{}", output);
				return output;
			} else {
				return null;
			}
		};
	}
}