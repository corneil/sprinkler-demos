package com.example.sprinkler.decision;

import java.time.Duration;
import java.util.Objects;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sprinkler.decision")
public class SprinklerDecisionProperties {
	Duration cycleDuration;

	Duration onDuration;

    Double minPrediction;

    Double minRainMeasure;

	public Duration getCycleDuration() {
		return cycleDuration;
	}

	public void setCycleDuration(Duration cycleDuration) {
		this.cycleDuration = cycleDuration;
	}

	public Duration getOnDuration() {
		return onDuration;
	}

	public void setOnDuration(Duration onDuration) {
		this.onDuration = onDuration;
	}

    public Double getMinPrediction() {
        return minPrediction;
    }

    public void setMinPrediction(Double minPrediction) {
        this.minPrediction = minPrediction;
    }

    public Double getMinRainMeasure() {
        return minRainMeasure;
    }

    public void setMinRainMeasure(Double minRainMeasure) {
        this.minRainMeasure = minRainMeasure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SprinklerDecisionProperties that = (SprinklerDecisionProperties) o;

        if (!Objects.equals(cycleDuration, that.cycleDuration)) return false;
        if (!Objects.equals(onDuration, that.onDuration)) return false;
        if (!Objects.equals(minPrediction, that.minPrediction)) return false;
		return Objects.equals(minRainMeasure, that.minRainMeasure);
	}

    @Override
    public int hashCode() {
        int result = cycleDuration != null ? cycleDuration.hashCode() : 0;
        result = 31 * result + (onDuration != null ? onDuration.hashCode() : 0);
        result = 31 * result + (minPrediction != null ? minPrediction.hashCode() : 0);
        result = 31 * result + (minRainMeasure != null ? minRainMeasure.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SprinklerDecisionProperties{" +
            "cycleDuration=" + cycleDuration +
            ", onDuration=" + onDuration +
            ", minPrediction=" + minPrediction +
            ", minRainMeasure=" + minRainMeasure +
            '}';
    }
}
