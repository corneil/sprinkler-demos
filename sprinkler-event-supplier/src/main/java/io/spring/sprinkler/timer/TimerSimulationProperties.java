package io.spring.sprinkler.timer;

import java.time.Duration;
import java.time.ZonedDateTime;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "simulation.event")
public class TimerSimulationProperties {

	/**
	 * The first date/time in the generated date range (ie. first event).
	 */
	private ZonedDateTime startTime;

	/**
	 * The amount of time the generated date range should cover (ie. 'startTime + runTime' is the last event).
	 */
	private Duration runTime;

	/**
	 * The amount of time between events (ie. ‘startTime + runTime / eventCycle' is the amount of events
	 * to generate during the simulation).
	 */
	private Duration eventCycle;

	/**
	 * The total amount of time the simulation should take to deliver all events.
	 */
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

	public Duration getEventCycle() {
		return eventCycle;
	}

	public void setEventCycle(Duration eventCycle) {
		this.eventCycle = eventCycle;
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
            ", cycleDuration=" + eventCycle +
            ", simulationTime=" + simulationTime +
            '}';
    }
}
