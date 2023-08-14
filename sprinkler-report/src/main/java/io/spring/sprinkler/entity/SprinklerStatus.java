package io.spring.sprinkler.entity;

import java.time.ZonedDateTime;

public record SprinklerStatus(ZonedDateTime statusDate, String state) {
}
