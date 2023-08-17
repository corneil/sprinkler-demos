package io.spring.sprinkler.common;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jetbrains.annotations.NotNull;

import org.springframework.lang.Nullable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SprinklerHistory(
    LocalDate date,
    @Nullable Double weatherPrediction,
    @Nullable Double rain,
    @Nullable LocalTime onTime,
    @Nullable Double runTime) implements Comparable<SprinklerHistory> {
    @Override
    public int compareTo(@NotNull SprinklerHistory o) {
        return date.compareTo(o.date);
    }
}
