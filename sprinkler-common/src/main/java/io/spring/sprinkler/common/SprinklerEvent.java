package io.spring.sprinkler.common;

import java.time.ZonedDateTime;

public record SprinklerEvent(String id, ZonedDateTime timestamp, SprinklerState state, String reason) {
    public String getId() {
        return id;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public SprinklerState getState() {
        return state;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return "SprinklerEvent{" +
            "id='" + id + '\'' +
            ", timestamp=" + timestamp +
            ", state=" + state +
            ", reason='" + reason + '\'' +
            '}';
    }
}
