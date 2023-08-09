package com.example.sprinkler.decision;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;
@SpringBootTest(classes = SprinklerDecisionApplication.class)
@ActiveProfiles("server")
class SprinklerDecisionApplicationTests {
    @Autowired
    protected SprinklerDecisionProperties decisionProperties;
	@Test
	public void contextLoads() {
	}

    @Test
    public void testProperties() {
        assertThat(decisionProperties.getOffTime()).isEqualTo(Duration.ofHours(47).plusMinutes(30));
        assertThat(decisionProperties.getOnTime()).isEqualTo(Duration.ofMinutes(30));
    }

}
