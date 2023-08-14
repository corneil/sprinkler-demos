package com.example.sprinkler.simulation_timer;

import java.time.Duration;
import java.time.ZonedDateTime;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "simulation.timer")
public class TimerSimulationProperties {
	// first time produced
	private ZonedDateTime startTime;
	// first time + runTime is the time of last simulation
	private Duration runTime;
	// represents the time of the event time between to events.
	private Duration cycleDuration;
	// represents to total time take to deliver all events.
	private Duration simulationTime;

	public Duration getRunTime() {
		return runTime;
	}

	public void setRunTime(Duration runTime) {
		this.runTime = runTime;
	}

	public ZonedDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(ZonedDateTime startTime) {
		this.startTime = startTime;
	}

	public Duration getCycleDuration() {
		return cycleDuration;
	}

	public void setCycleDuration(Duration cycleDuration) {
		this.cycleDuration = cycleDuration;
	}

	public Duration getSimulationTime() {
		return simulationTime;
	}

	public void setSimulationTime(Duration simulationTime) {
		this.simulationTime = simulationTime;
	}

    @Override
    public String toString() {
        return "TimerSimulationProperties{" +
            "startTime=" + startTime +
            ", runTime=" + runTime +
            ", cycleDuration=" + cycleDuration +
            ", simulationTime=" + simulationTime +
            '}';
    }
}
