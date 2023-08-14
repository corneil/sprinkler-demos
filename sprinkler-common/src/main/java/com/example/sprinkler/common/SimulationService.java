package com.example.sprinkler.common;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.lang.NonNull;
import org.springframework.lang.NonNullApi;

public interface SimulationService {
    Optional<SprinklerStatus> findLatestStatus(@NonNull ZonedDateTime timestamp);

    void updateSprinkler(SprinklerEvent event);

    /**
     * Provides the range for the input data
     *
     * @return the date range of the data
     */
    DateRange findDateRange();

    List<WeatherData> listAllWeather();
    List<SprinklerStatus> listAllStatus();
    Optional<WeatherData> latestWeather(ZonedDateTime timestamp);

    Double rainMeasuredFor(DateRange range);
}
