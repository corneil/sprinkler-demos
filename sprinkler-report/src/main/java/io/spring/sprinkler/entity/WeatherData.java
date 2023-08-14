package io.spring.sprinkler.entity;

import java.time.ZonedDateTime;

public record WeatherData(ZonedDateTime weatherTime, Double prediction, Double rainMeasured) {
}
