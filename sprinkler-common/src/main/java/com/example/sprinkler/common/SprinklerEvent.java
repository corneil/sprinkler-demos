package com.example.sprinkler.common;

import java.time.ZonedDateTime;
import java.util.Objects;

public class SprinklerEvent {
    private String id;

    private ZonedDateTime timestamp;

    private SprinklerState state;

    private String reason;

    public SprinklerEvent() {
    }

    public SprinklerEvent(String id, ZonedDateTime timestamp, SprinklerState state, String reason) {
        this.id = id;
        this.timestamp = timestamp;
        this.state = state;
        this.reason = reason;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public SprinklerState getState() {
        return state;
    }

    public void setState(SprinklerState state) {
        this.state = state;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SprinklerEvent that = (SprinklerEvent) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(timestamp, that.timestamp)) return false;
        if (state != that.state) return false;
        return Objects.equals(reason, that.reason);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (reason != null ? reason.hashCode() : 0);
        return result;
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
