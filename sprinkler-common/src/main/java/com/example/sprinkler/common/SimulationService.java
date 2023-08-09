package com.example.sprinkler.common;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface SimulationService {
    Optional<SprinklerStatus> findLatestStatus(ZonedDateTime timestamp);

    void updateSprinkler(SprinklerEvent event);

    /**
     * Provides the range for the input data
     *
     * @return the date range of the data
     */
    DateRange findDateRange();

    List<WeatherData> listAllWeather();

    Optional<WeatherData> latestWeather(ZonedDateTime timestamp);

    Double rainMeasuredFor(DateRange range);
}
