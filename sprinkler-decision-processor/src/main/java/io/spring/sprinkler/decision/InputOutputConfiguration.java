package io.spring.sprinkler.decision;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.spring.sprinkler.common.SprinklerEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

@Configuration
public class InputOutputConfiguration {
    private final static Logger logger = LoggerFactory.getLogger("io");

    @Bean
    public Function<Message<byte[]>, SprinklerEvent> parse(ObjectMapper mapper) {
        return input -> {
            if (input != null && input.getPayload() != null && input.getPayload().length > 0) {
                try {
                    logger.info("input:{}", input);
                    logger.info("input:payload:{}", new String(input.getPayload()));
                    return mapper.readValue(input.getPayload(), SprinklerEvent.class);
                } catch (IOException e) {
                    logger.error("Exception parsing:" + e, e);
                    throw new RuntimeException("Exception parsing:" + e, e);
                }
            } else {
                return null;
            }
        };
    }

    @Bean
    public Consumer<SprinklerEvent> write(ObjectMapper mapper, StreamBridge streamBridge) {
        return event -> {
            try {
                logger.info("output:{}", event);

                if (event != null) {
                    streamBridge.send("output", new GenericMessage<>(mapper.writeValueAsBytes(event)));
                }
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
