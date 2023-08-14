package com.example.sprinkler.common;

import java.time.ZonedDateTime;


public record SprinklerStatus(Long id, ZonedDateTime statusTime, SprinklerState state) {
    public Long getId() {
        return id;
    }

    public ZonedDateTime getStatusTime() {
        return statusTime;
    }

    public SprinklerState getState() {
        return state;
    }

    @Override
    public String toString() {
        return "SprinklerStatus{" +
            "id=" + id +
            ", statusTime=" + statusTime +
            ", state=" + state +
            '}';
    }
}
