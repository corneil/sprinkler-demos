package com.example.sprinkler.common;

import java.time.ZonedDateTime;

public record SprinklerEvent(String id, ZonedDateTime timestamp, SprinklerState state, String reason) {
}
