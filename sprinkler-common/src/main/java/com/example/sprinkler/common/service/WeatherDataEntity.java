package com.example.sprinkler.common.service;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

import com.example.sprinkler.common.WeatherData;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "weather_data")
public class WeatherDataEntity {
    @Column("weather_time")
    private Timestamp timestamp;

    private Double prediction;

    @Column("rain_measured")
    private Double rainMeasured;

    public WeatherDataEntity() {
    }

    public WeatherDataEntity(Timestamp timestamp, Double prediction, Double rainMeasured) {
        this.timestamp = timestamp;
        this.prediction = prediction;
        this.rainMeasured = rainMeasured;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Double getPrediction() {
        return prediction;
    }

    public void setPrediction(Double prediction) {
        this.prediction = prediction;
    }

    public Double getRainMeasured() {
        return rainMeasured;
    }

    public void setRainMeasured(Double rainMeasured) {
        this.rainMeasured = rainMeasured;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WeatherDataEntity that = (WeatherDataEntity) o;

        return Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return timestamp != null ? timestamp.hashCode() : 0;
    }

    public WeatherData toInfo() {
        return new WeatherData(timestamp.toInstant().atZone(ZoneId.of("UTC")), prediction, rainMeasured);
    }

    @Override
    public String toString() {
        return "WeatherDataEntity{" +
            "timestamp=" + timestamp +
            ", prediction=" + prediction +
            ", rainMeasured=" + rainMeasured +
            '}';
    }
}
