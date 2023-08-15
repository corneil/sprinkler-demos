package io.spring.sprinkler.decision;

import java.io.IOException;
import java.util.function.Function;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.spring.sprinkler.common.SprinklerEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

@Configuration
public class InputOutputConfiguration {
	private final static Logger logger = LoggerFactory.getLogger("io");

	@Bean
	public Function<Message<byte[]>, SprinklerEvent> bytesToEvent(ObjectMapper mapper) {
		return event -> {
			try {
				logger.info("input:{}", event);
				return mapper.readValue(event.getPayload(), SprinklerEvent.class);
			} catch (IOException e) {
				logger.error("Exception parsing:" + e, e);
				throw new RuntimeException("Exception parsing:" + e, e);
			}
		};
	}

	@Bean
	public Function<SprinklerEvent, Message<byte[]>> eventToBytes(ObjectMapper mapper) {
		return event -> {
			try {
				logger.info("output:{}", event);
				return new GenericMessage<>(mapper.writeValueAsBytes(event));
			} catch (JsonProcessingException e) {
				logger.error("Exception writing:" + e, e);
				throw new RuntimeException("Exception writing:" + e, e);
			}
		};
	}

	@Bean
	public ObjectMapper mapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.registerModule(new Jdk8Module());
		return mapper;
	}
}
