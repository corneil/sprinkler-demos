package com.example.sprinkler.sprinkler_data_consumer;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import javax.sql.DataSource;

import com.example.sprinkler.common.SimulationService;
import com.example.sprinkler.common.SprinklerEvent;
import com.example.sprinkler.common.SprinklerState;
import com.example.sprinkler.common.SprinklerStatus;
import com.example.sprinkler.common.service.SprinklerServiceConfig;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = SprinklerDataConsumerTests.TestApplication.class)
@TestPropertySource(locations = "classpath:test.properties")
@ActiveProfiles("server")
class SprinklerDataConsumerTests {
    @Autowired
    protected Consumer<SprinklerEvent> dataConsumer;

    @Autowired
    protected SimulationService simulationService;

    @Test
    void testData() {
        ZonedDateTime now = ZonedDateTime.parse("2023-08-02T01:00:00+02:00");
        SprinklerEvent event = new SprinklerEvent(UUID.randomUUID().toString(), now, SprinklerState.ON, "TEST");
        dataConsumer.accept(event);
        Optional<SprinklerStatus> status = simulationService.findLatestStatus(now);
        assertThat(status).isPresent();
        assertThat(status.get().getStatusTime().toInstant()).isEqualTo(event.getTimestamp().toInstant());
        assertThat(status.get().getState()).isEqualByComparingTo(event.getState());
    }

    @SpringBootApplication
    @Import(SprinklerServiceConfig.class)
    @EnableTransactionManagement
    @AutoConfigureDataJdbc
    @EnableConfigurationProperties(LiquibaseProperties.class)
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
