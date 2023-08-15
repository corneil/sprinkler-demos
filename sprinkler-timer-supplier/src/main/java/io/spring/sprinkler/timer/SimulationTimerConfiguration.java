package io.spring.sprinkler.timer;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.Supplier;

import io.spring.sprinkler.common.DateRange;
import io.spring.sprinkler.common.SimulationService;
import io.spring.sprinkler.common.SprinklerEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.util.Assert;

@Configuration
@EnableConfigurationProperties(TimerSimulationProperties.class)
public class SimulationTimerConfiguration {
	private final static Logger logger = LoggerFactory.getLogger(SimulationTimerConfiguration.class);

	final UUID uuid = UUID.randomUUID();

	ZonedDateTime next = null;

	ZonedDateTime last = null;

	long eventCount = 0;

	long eventRate = 0;

	private void configure(
		TimerSimulationProperties properties,
		@Autowired(required = false) SimulationService simulationService
	) {
		logger.info("configure:{}", properties);
		Duration totalRunTime = properties.getRunTime();
		if (simulationService != null) {
			DateRange range = simulationService.findDateRange();
			totalRunTime = Duration.between(range.getStart(), range.getEnd());
			logger.info("simulationService:dateRange={}, total run time:{}", range, totalRunTime);
			next = range.getStart();
			last = range.getEnd();
		} else {
			next = properties.getStartTime();
			last = properties.getStartTime().plus(totalRunTime);
		}
		Assert.isTrue(totalRunTime.toMillis() > properties.getEventCycle().toMillis(), "Total run time must be larger than cycleDuration");
		eventCount = totalRunTime.toMillis() / properties.getEventCycle().toMillis();
		long minSimTime = Math.max(eventCount, properties.getSimulationTime().toMillis());
		logger.info("eventCount={}, simulationTime={}ms", eventCount, minSimTime);
		eventRate = Math.max(minSimTime / eventCount, 1L);
		logger.info("fixed-rate={}ms", eventRate);
	}

	@Bean(name = "timer")
	@Primary
	public Supplier<Flux<Message<SprinklerEvent>>> timer(
		TimerSimulationProperties properties,
		@Autowired(required = false) SimulationService simulationService
	) {
		if (last == null || next == null) {
			configure(properties, simulationService);
		}
		return () -> Mono.<Message<SprinklerEvent>>create(monoSink ->
				monoSink.onRequest(value -> monoSink.success(this.createEvent(properties, simulationService))))
			.subscribeOn(Schedulers.immediate())
			.delayElement(Duration.ofMillis(eventRate))
			.repeat(eventCount);
	}

	private Message<SprinklerEvent> createEvent(TimerSimulationProperties properties, @Autowired(required = false) SimulationService simulationService) {

		logger.info("properties:{}", properties);
		logger.info("next={}, last={}", next, last);
		if (last == null || last.isAfter(next)) {
			SprinklerEvent event = new SprinklerEvent(uuid.toString(), next, null, null);
			logger.info("event:{}", event);
			next = next.plus(properties.getEventCycle());
			return new GenericMessage<>(event);
		}
		logger.info("event:null");
		return null;
	}
}
