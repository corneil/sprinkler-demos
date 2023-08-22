package io.spring.sprinkler.app;

import io.spring.sprinkler.common.service.SprinklerServiceConfig;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SuppressWarnings("resource")
@TestConfiguration(proxyBeanMethods = false)
@Import(SprinklerServiceConfig.class)
public class TestSprinklerSimulationApplication {

    @Bean
    @ServiceConnection
    MariaDBContainer<?> mariaDbContainer() {
        return new MariaDBContainer<>(DockerImageName.parse("mariadb:10.6"));
    }

    @Bean
    @ServiceConnection
    RabbitMQContainer rabbitContainer() {
        return new RabbitMQContainer(DockerImageName.parse("rabbitmq:3.8-management"));
    }

    private int skipperPort;

    private String skipperHost;

    @Bean
    @ServiceConnection
    GenericContainer<?> skipperContainer() {
        return new GenericContainer<>(DockerImageName.parse("springcloud/spring-cloud-skipper-server:2.11.0-RC1"));
    }

    @Bean
    @ServiceConnection
    GenericContainer<?> scdfContainer() {
        GenericContainer<?> skipper = skipperContainer();
        return new GenericContainer<>(DockerImageName.parse("springcloud/spring-cloud-dataflow-server:2.11.0-RC1"))
            .withEnv("SPRING_CLOUD_SKIPPER_CLIENT_SERVER_URI", String.format("http://%s:%d", skipper.getHost(), skipper.getMappedPort(7577)));
    }



    public static void main(String[] args) {
        SpringApplication.from(TestSprinklerSimulationApplication::main).with(TestSprinklerSimulationApplication.class).run(args);
    }

}
