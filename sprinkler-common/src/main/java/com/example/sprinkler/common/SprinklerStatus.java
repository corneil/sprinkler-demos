package com.example.sprinkler.common;

import java.time.ZonedDateTime;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class SprinklerStatus {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private ZonedDateTime statusTime;
	@Enumerated(EnumType.STRING)
	private SprinklerState state;

	public SprinklerStatus() {
	}

	public SprinklerStatus(Long id, ZonedDateTime statusTime, SprinklerState state) {
		this.id = id;
		this.statusTime = statusTime;
		this.state = state;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ZonedDateTime getStatusTime() {
		return statusTime;
	}

	public void setStatusTime(ZonedDateTime statusTime) {
		this.statusTime = statusTime;
	}

	public SprinklerState getState() {
		return state;
	}

	public void setState(SprinklerState state) {
		this.state = state;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SprinklerStatus that = (SprinklerStatus) o;

		if (!Objects.equals(id, that.id)) return false;
		if (!Objects.equals(statusTime, that.statusTime)) return false;
		return state == that.state;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (statusTime != null ? statusTime.hashCode() : 0);
		result = 31 * result + (state != null ? state.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "SprinklerStatus{" +
			"id=" + id +
			", statusTime=" + statusTime +
			", state=" + state +
			'}';
	}
}

