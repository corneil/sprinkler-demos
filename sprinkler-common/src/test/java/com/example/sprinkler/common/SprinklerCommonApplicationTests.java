package com.example.sprinkler.common;

import org.junit.jupiter.api.Test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootTest(classes = SprinklerCommonApplicationTests.TestApplication.class)
class SprinklerCommonApplicationTests {

    @Test
    void contextLoads() {
    }

    @SpringBootApplication
    @Import(SprinklerCommonConfig.class)
    @EnableTransactionManagement
    public static class TestApplication {
        public static void main(String[] args) {
            SpringApplication.run(TestApplication.class, args);
        }
    }
}
