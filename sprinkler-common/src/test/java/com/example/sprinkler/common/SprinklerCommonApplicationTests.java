package com.example.sprinkler.common;

import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;

import com.example.sprinkler.common.service.SprinklerServiceConfig;
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
        assertThat(range.getStart().toInstant()).isBeforeOrEqualTo(Timestamp.valueOf("2023-08-01 23:59:59").toInstant());
        assertThat(range.getEnd().toInstant()).isAfterOrEqualTo(Timestamp.valueOf("2023-08-14 23:59:59").toInstant());
    }

    @Test
    void testLatestData() {
        ZonedDateTime now = Timestamp.valueOf("2023-08-04 13:00:00").toInstant().atZone(ZoneOffset.UTC);
        Optional<WeatherData> latest = simulationService.latestWeather(now);
        assertThat(latest).isPresent();
        assertThat(latest.get().getTimestamp()).isBeforeOrEqualTo(now);
        assertThat(latest.get().getTimestamp()).isAfterOrEqualTo(Timestamp.valueOf("2023-08-03 23:59:59").toInstant().atZone(ZoneOffset.UTC));
        assertThat(latest.get().getPrediction()).isCloseTo(0.15, Offset.offset(0.0001));
        assertThat(latest.get().getRainMeasured()).isCloseTo(0.3, Offset.offset(0.0001));
    }

    @Test
    void testRainData() {
        ZonedDateTime now = Timestamp.valueOf("2023-08-04 13:00:00").toInstant().atZone(ZoneOffset.UTC);
        Double rainMeasure = simulationService.rainMeasuredFor(new DateRange(now.minusDays(2), now));
        assertThat(rainMeasure).isNotNull();
        assertThat(rainMeasure).isCloseTo(3.0, Offset.offset(0.01));
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
    void testNoState() {
        ZonedDateTime now = Timestamp.valueOf("2023-08-01 13:00:00").toInstant().atZone(ZoneOffset.UTC);
        Optional<SprinklerStatus> state = simulationService.findLatestStatus(now);
        assertThat(state).isNotPresent();
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
