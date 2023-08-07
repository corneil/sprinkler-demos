package com.example.sprinkler.decision;

import java.time.Duration;
import java.util.Objects;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sprinkler.timer")
public class SprinklerDecisionProperties {
	Duration offTime;

	Duration onTime;

	public Duration getOffTime() {
		return offTime;
	}

	public void setOffTime(Duration offTime) {
		this.offTime = offTime;
	}

	public Duration getOnTime() {
		return onTime;
	}

	public void setOnTime(Duration onTime) {
		this.onTime = onTime;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SprinklerDecisionProperties that = (SprinklerDecisionProperties) o;

		if (!Objects.equals(offTime, that.offTime)) return false;
		return Objects.equals(onTime, that.onTime);
	}

	@Override
	public int hashCode() {
		int result = offTime != null ? offTime.hashCode() : 0;
		result = 31 * result + (onTime != null ? onTime.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "SprinklerDecisionProperties{" +
			"offTime=" + offTime +
			", onTime=" + onTime +
			'}';
	}
}
