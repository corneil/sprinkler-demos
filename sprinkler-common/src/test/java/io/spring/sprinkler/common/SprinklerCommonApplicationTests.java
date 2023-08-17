package io.spring.sprinkler.common;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import io.spring.sprinkler.common.service.SprinklerServiceConfig;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.predicate;

@SpringBootTest(classes = SprinklerCommonApplicationTests.TestApplication.class)
@TestPropertySource(locations = "classpath:test.properties")
@ActiveProfiles("server")
class SprinklerCommonApplicationTests {
	@Autowired
	protected SimulationService simulationService;

	@Test
	void contextLoads() {
	}

	@Test
	void testRange() {
		DateRange range = simulationService.findDateRange();
		assertThat(range.getStart().toInstant()).isBeforeOrEqualTo(Timestamp.valueOf("2023-08-01 06:00:00").toInstant());
		assertThat(range.getEnd().toInstant()).isAfterOrEqualTo(Timestamp.valueOf("2023-08-14 00:00:00").toInstant());
	}

	@Test
	void testLatestData() {
		ZonedDateTime now = Timestamp.valueOf("2023-08-04 13:00:00").toInstant().atZone(ZoneOffset.UTC);
		Optional<WeatherData> latest = simulationService.latestWeather(now);
		assertThat(latest).isPresent();
		assertThat(latest.get().getTimestamp()).isBeforeOrEqualTo(now);
		assertThat(latest.get().getTimestamp()).isAfterOrEqualTo(Timestamp.valueOf("2023-08-04 00:00:00").toInstant().atZone(ZoneOffset.UTC));
		assertThat(latest.get().getPrediction()).isCloseTo(0.81, Offset.offset(0.0001));
		assertThat(latest.get().getRainMeasured()).isCloseTo(3.8, Offset.offset(0.0001));
	}

	@Test
	void testRainData() {
		ZonedDateTime now = Timestamp.valueOf("2023-08-04 13:00:00").toInstant().atZone(ZoneOffset.UTC);
		Double rainMeasure = simulationService.rainMeasuredFor(new DateRange(now.minusDays(2), now));
		assertThat(rainMeasure).isNotNull();
		assertThat(rainMeasure).isCloseTo(4.1, Offset.offset(0.01));
	}

	@Test
	void testState() {
		ZonedDateTime now = Timestamp.valueOf("2023-08-04 13:00:00").toInstant().atZone(ZoneOffset.UTC);
		Optional<SprinklerStatus> state = simulationService.findLatestStatus(now);
		assertThat(state).isPresent();
		assertThat(state.get().getState()).isEqualByComparingTo(SprinklerState.OFF);
	}

	@Test
	void testFirstState() {
		ZonedDateTime now = Timestamp.valueOf("2023-08-01 23:59:59").toInstant().atZone(ZoneOffset.UTC);
		Optional<SprinklerStatus> state = simulationService.findLatestStatus(now);
		assertThat(state).isPresent();
		assertThat(state.get().getState()).isEqualByComparingTo(SprinklerState.OFF);
	}

	@Test
	void testLastAndUpdate() {
		ZonedDateTime now = Timestamp.valueOf("2023-07-30 00:00:00").toInstant().atZone(ZoneOffset.UTC);
		Optional<SprinklerStatus> state = simulationService.findLatestStatus(now);
		assertThat(state).isNotPresent();
		SprinklerState sprinklerState = SprinklerState.ON;
		SprinklerEvent event = new SprinklerEvent(UUID.randomUUID().toString(), now, sprinklerState, "Test");
		simulationService.updateSprinkler(event);
	}
    @Test
    void testHistory() {
        ZonedDateTime now = Timestamp.valueOf("2023-08-03 00:00:00").toInstant().atZone(ZoneOffset.UTC);
        Optional<SprinklerStatus> state = simulationService.findLatestStatus(now);
        assertThat(state).isPresent();
        String id = UUID.randomUUID().toString();
        simulationService.updateSprinkler(new SprinklerEvent(id, now, SprinklerState.ON, "Test"));
        simulationService.updateSprinkler(new SprinklerEvent(id, now.plusMinutes(15), SprinklerState.OFF, "Test"));
        state = simulationService.findLatestStatus(now.plusDays(2));
        assertThat(state).isPresent();
        simulationService.updateSprinkler(new SprinklerEvent(id, now.plusDays(2), SprinklerState.ON, "Test"));
        simulationService.updateSprinkler(new SprinklerEvent(id, now.plusDays(2).plusMinutes(15), SprinklerState.OFF, "Test"));
        List<SprinklerHistory> history = simulationService.listHistory();
        assertThat(history).isNotEmpty();
        assertThat(history).hasSize(15);
        assertThat(history).isSorted();
    }

	@SpringBootApplication
	@Import(SprinklerServiceConfig.class)
	@EnableTransactionManagement
	public static class TestApplication {
		public static void main(String[] args) {
			SpringApplication.run(TestApplication.class, args);
		}

	}
}
