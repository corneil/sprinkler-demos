package io.spring.sprinkler.timer;

import java.util.List;
import java.util.function.Supplier;

import io.spring.sprinkler.common.SprinklerEvent;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = SimulationTimerGenerationTests.TestApplication.class)
@TestPropertySource(properties = {
	"simulation.event.event-cycle=PT30M",
	"simulation.event.start-time=2023-08-01T23:59:59.000+00:00",
	"simulation.event.run-time=P2D",
	"simulation.event.simulation-time=PT10S"
})
class SimulationTimerGenerationTests {
	@Autowired
	@Qualifier("event")
	protected Supplier<Flux<Message<SprinklerEvent>>> timeSupplierFlux;

	@Test
	void readTest() {
		List<Message<SprinklerEvent>> messages = timeSupplierFlux.get().collectList().block();
		assertThat(messages).isNotEmpty();
		assertThat(messages).hasSize(24 * 60 / 30 * 2);
	}

	@SpringBootApplication
	@EnableConfigurationProperties(TimerSimulationProperties.class)
	@EnableTransactionManagement
	public static class TestApplication {
		public static void main(String[] args) {
			SpringApplication.run(TestApplication.class, args);
		}
	}
}
