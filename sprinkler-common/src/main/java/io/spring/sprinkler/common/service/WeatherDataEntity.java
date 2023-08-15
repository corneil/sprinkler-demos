package io.spring.sprinkler.common.service;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.Objects;

import io.spring.sprinkler.common.WeatherData;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "WEATHER_DATA")
public class WeatherDataEntity {
    @Column("WEATHER_TIME")
    private Timestamp timestamp;
    @Column("PREDICTION")
    private Double prediction;

    @Column("RAIN_MEASURED")
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
