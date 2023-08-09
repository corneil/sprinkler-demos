package com.example.sprinkler.common;

import java.time.ZonedDateTime;
import java.time.ZonedDateTime;

public record WeatherData(ZonedDateTime timestamp, Double prediction, Double rainMeasured) {
}
