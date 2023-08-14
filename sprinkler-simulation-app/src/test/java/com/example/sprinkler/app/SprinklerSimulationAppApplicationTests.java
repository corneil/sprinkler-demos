package com.example.sprinkler.app;


import java.util.Optional;
import java.util.UUID;

import com.example.sprinkler.common.DateRange;
import com.example.sprinkler.common.SimulationService;
import com.example.sprinkler.common.SprinklerEvent;
import com.example.sprinkler.common.SprinklerState;
import com.example.sprinkler.common.SprinklerStatus;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TestSprinklerSimulationAppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("server")
class SprinklerSimulationAppApplicationTests {
    @BeforeAll
    public static void initContainer() {
        TestSprinklerSimulationAppApplication.container.start();
    }

    @Autowired
    protected SimulationService simulationService;
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @BeforeEach
    public void setupMocks() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
            .defaultRequest(get("/").accept(MediaType.APPLICATION_JSON)).build();
    }
    @Test
    void contextLoads() throws Exception {
        ResultActions result = mockMvc.perform(get("/api").accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
        result.andDo(print()).andExpect(jsonPath("$.version", is("1.0.0-SNAPSHOT")));
        DateRange dateRange = simulationService.findDateRange();
        simulationService.latestWeather(dateRange.getStart());
        Double measured = simulationService.rainMeasuredFor(dateRange);
        assertThat(measured).isNotNull();
        assertThat(measured).isCloseTo(31.3, Offset.offset(0.001));

        SprinklerEvent event = new SprinklerEvent(UUID.randomUUID().toString(), dateRange.getStart().plusDays(1), SprinklerState.ON, "Test");
        simulationService.updateSprinkler(event);
        Optional<SprinklerStatus> status = simulationService.findLatestStatus(dateRange.getEnd());
        assertThat(status).isPresent();
        assertThat(status.get().getStatusTime()).isEqualTo(event.getTimestamp());
        assertThat(status.get().getState()).isEqualByComparingTo(event.getState());
    }

}
