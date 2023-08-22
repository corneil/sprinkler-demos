package io.spring.sprinkler.common.service;

import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.util.Objects;

import io.spring.sprinkler.common.SprinklerState;
import io.spring.sprinkler.common.SprinklerStatus;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "SPRINKLER_STATE")
public class SprinklerStatusEntity {
    @Id
    @Column("ID")
    private Long id;

    @Column("STATUS_TIME")
    private Timestamp statusTime;

    @Column("STATE")
    private SprinklerState state;

    @Column("REASON")
    private String reason;

    public SprinklerStatusEntity() {
    }

    public SprinklerStatusEntity(Timestamp statusTime, SprinklerState state, String reason) {
        this.statusTime = statusTime;
        this.state = state;
        this.reason = reason;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getStatusTime() {
        return statusTime;
    }

    public void setStatusTime(Timestamp statusTime) {
        this.statusTime = statusTime;
    }

    public SprinklerState getState() {
        return state;
    }

    public void setState(SprinklerState state) {
        this.state = state;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SprinklerStatusEntity that = (SprinklerStatusEntity) o;

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

    public SprinklerStatus toInfo() {
        return new SprinklerStatus(id, statusTime.toInstant().atZone(ZoneOffset.UTC), state, reason);
    }

    @Override
    public String toString() {
        return "SprinklerStatusEntity{" +
            "id=" + id +
            ", statusTime=" + statusTime +
            ", state=" + state +
            ", reason='" + reason + '\'' +
            '}';
    }
}

