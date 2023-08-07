import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.function.Function;

import javax.persistence.Entity;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.Repository;

class Scratch {
	public static void main(String[] args) {

	}


	@Bean
	public Function<SprinklerEvent, SprinklerEvent> timer(SprinklerStatusRepository statusRepository, SprinklerDecisionProperties properties) {
		return (input) -> {
			SprinklerStatus status = statusRepository.findFirstOrderByStatusEventDesc();
			if (status.state.equals(SprinklerState.OFF)) {
				Instant offTime = input.timestamp.toInstant().plus(properties.offTime);
				if (status.statusTime.toInstant().isBefore(offTime)) {
					return new SprinklerEvent(input.id, input.timestamp, SprinklerState.ON,
						input.reason + ": timer " + properties.offTime.toString());
				}
			}
			return null;
		};
	}
}

class SprinklerDecisionProperties {
	Duration offTime;

	Duration onTime;
}

interface SprinklerStatusRepository implements Repository<SprinklerStatus, Long> {
	SprinklerStatus findFirstOrderByStatusEventDesc();
}

@Entity
class SprinklerStatus {
	Long id;

	ZonedDateTime statusTime;

	SprinklerState state;
}

class SprinklerEvent {
	String id;

	ZonedDateTime timestamp;

	String reason;

	SprinklerState state;

	public SprinklerEvent(String id, ZonedDateTime timestamp, SprinklerState state, String reason) {
		this.id = id;
		this.timestamp = timestamp;
		this.reason = reason;
		this.state = state;
	}
	// getters and setters
}

enum SprinklerState {
	OFF,
	ON
}