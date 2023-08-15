package io.spring.sprinkler.timer;

import io.spring.sprinkler.common.service.SprinklerServiceConfig;
import org.junit.jupiter.api.Test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootTest(classes = SimulationTimerApplicationTests.TestApplication.class)
@ActiveProfiles("server")
class SimulationTimerApplicationTests {

    @Test
    void contextLoads() {
    }

    @SpringBootApplication
    @Import({SprinklerServiceConfig.class, SimulationTimerConfiguration.class})
    @EnableConfigurationProperties(TimerSimulationProperties.class)
    @EnableTransactionManagement
    public static class TestApplication {
        public static void main(String[] args) {
            SpringApplication.run(TestApplication.class, args);
        }

    }
}
