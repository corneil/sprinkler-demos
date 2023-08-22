package io.spring.sprinkler.app;


import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.spring.sprinkler.common.DateRange;
import io.spring.sprinkler.common.SimulationService;
import io.spring.sprinkler.common.SprinklerEvent;
import io.spring.sprinkler.common.SprinklerHistory;
import io.spring.sprinkler.common.SprinklerState;
import io.spring.sprinkler.common.SprinklerStatus;
import io.spring.sprinkler.common.WeatherData;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("server")
class SprinklerSimulationAppApplicationTests {


	@Autowired
	protected SimulationService simulationService;

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	ObjectMapper mapper;

	@BeforeEach
	public void setupMocks() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
			.defaultRequest(get("/").accept(MediaType.APPLICATION_JSON)).build();
	}

	@Test
	void testApi() throws Exception {
		ResultActions result = mockMvc.perform(get("/api")
				.accept(MediaType.APPLICATION_JSON)
			).andDo(print())
			.andExpect(status().isOk());
		result.andDo(print())
			.andExpect(jsonPath("$.version", is("1.0.0-SNAPSHOT")));
		result = mockMvc.perform(get("/api/date-range").accept(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk());
		MockHttpServletResponse response = result.andReturn().getResponse();
		String content = response.getContentAsString();
		assertThat(content).isNotBlank();
		DateRange dateRange = mapper.readValue(content, DateRange.class);
		result = mockMvc.perform(get("/api/latest-on")
				.queryParam("timestamp", dateRange.getStart().toString())
				.accept(MediaType.APPLICATION_JSON)
			).andDo(print())
			.andExpect(status().isOk());
		response = result.andReturn().getResponse();
		content = response.getContentAsString();
		assertThat(content).isNotBlank();
		WeatherData latest = mapper.readValue(content, WeatherData.class);
		assertThat(latest).isNotNull();
		result = mockMvc.perform(get("/api/rain")
				.queryParam("start", dateRange.getStart().toString())
				.queryParam("end", dateRange.getEnd().toString())
				.accept(MediaType.APPLICATION_JSON)
			).andDo(print())
			.andExpect(status().isOk());
		response = result.andReturn().getResponse();
		content = response.getContentAsString();
		assertThat(content).isNotBlank();
		Double measured = mapper.readValue(content, Double.TYPE);
		assertThat(measured).isNotNull();
		assertThat(measured).isCloseTo(31.3, Offset.offset(0.001));

		SprinklerEvent event = new SprinklerEvent(UUID.randomUUID().toString(), dateRange.getStart().plusDays(1), SprinklerState.ON, "Test");
		mockMvc.perform(post("/api/update")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(event))
				.accept(MediaType.APPLICATION_JSON)
			).andDo(print())
			.andExpect(status().isOk());
		result = mockMvc.perform(get("/api/latest")
				.accept(MediaType.APPLICATION_JSON)
				.queryParam("timestamp", dateRange.getEnd().toString())
			).andDo(print())
			.andExpect(status().isOk());
		response = result.andReturn().getResponse();
		content = response.getContentAsString();
		assertThat(content).isNotBlank();
		SprinklerStatus status = mapper.readValue(content, SprinklerStatus.class);
		assertThat(status).isNotNull();
		assertThat(status.getStatusTime()).isEqualTo(event.getTimestamp());
		assertThat(status.getState()).isEqualByComparingTo(event.getState());
		result = mockMvc.perform(get("/api/status")
				.accept(MediaType.APPLICATION_JSON)
			).andDo(print())
			.andExpect(status().isOk());
		response = result.andReturn().getResponse();
		content = response.getContentAsString();
		assertThat(content).isNotBlank();
		SprinklerStatus[] allStatus = mapper.readValue(content, SprinklerStatus[].class);
		assertThat(allStatus).isNotNull();
		assertThat(allStatus).isNotEmpty();
		assertThat(allStatus).hasAtLeastOneElementOfType(SprinklerStatus.class);
        result = mockMvc.perform(get("/api/history")
                .accept(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isOk());
        response = result.andReturn().getResponse();
        content = response.getContentAsString();
        assertThat(content).isNotBlank();
        SprinklerHistory [] history = mapper.readValue(content, SprinklerHistory[].class);
        assertThat(history).isNotNull();
        assertThat(history.length).isGreaterThan(0);
		mockMvc.perform(post("/api/reset"))
			.andDo(print())
			.andExpect(status().isOk());
		result = mockMvc.perform(get("/api/status")
				.accept(MediaType.APPLICATION_JSON)
			).andDo(print())
			.andExpect(status().isOk());
		response = result.andReturn().getResponse();
		content = response.getContentAsString();
		assertThat(content).isNotBlank();
		allStatus = mapper.readValue(content, SprinklerStatus[].class);
		assertThat(allStatus).isNotNull();
		assertThat(allStatus).hasSize(1);
		assertThat(allStatus).hasAtLeastOneElementOfType(SprinklerStatus.class);
	}

}
