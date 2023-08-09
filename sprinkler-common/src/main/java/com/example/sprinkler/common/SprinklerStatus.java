package com.example.sprinkler.common;

import java.time.ZonedDateTime;


public record SprinklerStatus(Long id, ZonedDateTime statusTime, SprinklerState state) {
}
