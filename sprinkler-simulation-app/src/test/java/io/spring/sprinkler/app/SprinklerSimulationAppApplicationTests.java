package io.spring.sprinkler.app;


import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.spring.sprinkler.common.DateRange;
import io.spring.sprinkler.common.SimulationService;
import io.spring.sprinkler.common.SprinklerEvent;
import io.spring.sprinkler.common.SprinklerState;
import io.spring.sprinkler.common.SprinklerStatus;
import io.spring.sprinkler.common.WeatherData;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.utility.DockerImageName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
	static final MariaDBContainer<?> container = new MariaDBContainer<>(DockerImageName.parse("mariadb:latest"));

	@DynamicPropertySource
	static void databaseProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", container::getJdbcUrl);
		registry.add("spring.datasource.username", container::getUsername);
		registry.add("spring.datasource.password", container::getPassword);
		registry.add("spring.datasource.driver-class-name", container::getDriverClassName);
	}

	@BeforeAll
	public static void initContainer() {
		container.start();
	}

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
	void contextLoads() throws Exception {
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
	}

}
