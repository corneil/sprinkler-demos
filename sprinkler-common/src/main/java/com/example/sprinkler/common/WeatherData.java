package com.example.sprinkler.common;

import java.time.ZonedDateTime;

public record WeatherData(ZonedDateTime timestamp, Double prediction, Double rainMeasured) {
    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public Double getPrediction() {
        return prediction;
    }

    public Double getRainMeasured() {
        return rainMeasured;
    }

    @Override
    public String toString() {
        return "WeatherData{" +
            "timestamp=" + timestamp +
            ", prediction=" + prediction +
            ", rainMeasured=" + rainMeasured +
            '}';
    }
}
