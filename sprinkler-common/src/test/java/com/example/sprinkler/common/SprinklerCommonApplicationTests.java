package com.example.sprinkler.common;

import java.time.ZonedDateTime;
import java.util.Optional;

import javax.sql.DataSource;

import com.example.sprinkler.common.service.SprinklerServiceConfig;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
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
        assertThat(range.start()).isBeforeOrEqualTo(ZonedDateTime.parse("2023-08-01T23:59:59+02:00"));
        assertThat(range.end()).isAfterOrEqualTo(ZonedDateTime.parse("2023-08-14T23:59:59+02:00"));
    }

    @Test
    void testLatestData() {
        ZonedDateTime now = ZonedDateTime.parse("2023-08-04T13:00:00+02:00");
        Optional<WeatherData> latest = simulationService.latestWeather(now);
        assertThat(latest).isPresent();
        assertThat(latest.get().timestamp()).isBeforeOrEqualTo(now);
        assertThat(latest.get().timestamp()).isAfterOrEqualTo(ZonedDateTime.parse("2023-08-03T23:59:59+02:00"));
        assertThat(latest.get().prediction()).isCloseTo(0.15, Offset.offset(0.0001));
        assertThat(latest.get().rainMeasured()).isCloseTo(0.3, Offset.offset(0.0001));
    }

    @SpringBootApplication
    @Import(SprinklerServiceConfig.class)
    @EnableTransactionManagement
    public static class TestApplication {
        public static void main(String[] args) {
            SpringApplication.run(TestApplication.class, args);
        }
        @Bean
        public DataSource dataSource() {
            return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
        }
    }
}
