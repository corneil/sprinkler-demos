package io.spring.sprinkler.decision;

import java.time.Duration;
import java.util.UUID;
import java.util.function.Function;

import javax.sql.DataSource;

import io.spring.sprinkler.common.DateRange;
import io.spring.sprinkler.common.SimulationService;
import io.spring.sprinkler.common.SprinklerEvent;
import io.spring.sprinkler.common.SprinklerState;
import io.spring.sprinkler.common.service.SprinklerServiceConfig;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

import static org.assertj.core.api.Assertions.*;
@SpringBootTest(classes = SprinklerDecisionFunctionTests.TestApplication.class)
@TestPropertySource(locations = "classpath:test.properties")
@ActiveProfiles("server")
class SprinklerDecisionFunctionTests {
    private final static Logger logger = LoggerFactory.getLogger(SprinklerDecisionFunctionTests.class);
    @Autowired
    protected SprinklerDecisionProperties decisionProperties;
    @Autowired
    protected SimulationService simulationService;
    @Autowired
    Function<SprinklerEvent, SprinklerEvent> timerRule;
	@Test
	public void contextLoads() {
	}

    @Test
    public void testProperties() {
        assertThat(decisionProperties.getCycleDuration()).isEqualTo(Duration.ofHours(48));
        assertThat(decisionProperties.getOnDuration()).isEqualTo(Duration.ofMinutes(30));
    }
    @Test
    public void testTimer() {
        DateRange range = simulationService.findDateRange();
        SprinklerEvent input = new SprinklerEvent(UUID.randomUUID().toString(), range.getStart(), null, null);
        logger.info("testTimer:input={}", input);
        SprinklerEvent result = timerRule.apply(input);
        logger.info("testTimer:result={}", result);
        assertThat(result).isNotNull();
        assertThat(result.getState()).isEqualByComparingTo(SprinklerState.OFF);
        SprinklerEvent input2 = new SprinklerEvent(result.getId(), range.getStart().plus(Duration.ofHours(1)), result.getState(), null);
        logger.info("testTimer:input2={}", input2);
        SprinklerEvent result2 = timerRule.apply(input2);
        logger.info("testTimer:result2={}", result2);
        assertThat(result2).isNotNull();
        assertThat(result2.getState()).isEqualByComparingTo(SprinklerState.ON);
    }


    @SpringBootApplication
    @Import(SprinklerServiceConfig.class)
    @EnableTransactionManagement
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
