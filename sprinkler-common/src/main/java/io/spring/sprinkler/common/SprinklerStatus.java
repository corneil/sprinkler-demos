package io.spring.sprinkler.common;

import java.time.ZonedDateTime;


public record SprinklerStatus(Long id, ZonedDateTime statusTime, SprinklerState state, String reason) {
    public Long getId() {
        return id;
    }

    public ZonedDateTime getStatusTime() {
        return statusTime;
    }

    public SprinklerState getState() {
        return state;
    }
    public String getReason() { return reason; }

    @Override
    public String toString() {
        return "SprinklerStatus{" +
            "id=" + id +
            ", statusTime=" + statusTime +
            ", state=" + state +
            ", reason='" + reason + '\'' +
            '}';
    }
}
