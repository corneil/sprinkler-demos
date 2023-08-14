package com.example.sprinkler.common.service;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.example.sprinkler.common.DateRange;
import com.example.sprinkler.common.SimulationService;
import com.example.sprinkler.common.SprinklerEvent;
import com.example.sprinkler.common.SprinklerStatus;
import com.example.sprinkler.common.WeatherData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

public class SimulationServiceImpl implements SimulationService {
    private static final Logger logger = LoggerFactory.getLogger(SimulationServiceImpl.class);

    private final SprinklerStatusRepository statusRepository;

    private final WeatherDataRepository weatherDataRepository;

    public SimulationServiceImpl(SprinklerStatusRepository statusRepository, WeatherDataRepository weatherDataRepository) {
        this.statusRepository = statusRepository;
        this.weatherDataRepository = weatherDataRepository;
    }

    @Override
    public Optional<SprinklerStatus> findLatestStatus(ZonedDateTime timestamp) {
        Assert.notNull(timestamp, "timestamp required");
        Optional<SprinklerStatus> sprinklerStatus = statusRepository.findLatestFor(Timestamp.from(timestamp.toInstant()))
            .map(SprinklerStatusEntity::toInfo);
        logger.debug("findLatestStatus:{}", sprinklerStatus);
        return sprinklerStatus;
    }

    @Override
    public void updateSprinkler(@NonNull SprinklerEvent event) {
        Assert.notNull(event, "event required");
        Optional<SprinklerStatusEntity> latest = statusRepository.findLatestFor(Timestamp.from(event.getTimestamp().toInstant()));
        logger.debug("updateSprinkler:latest={}", latest.orElse(null));
        if (latest.isEmpty() || (latest.get().getStatusTime().toInstant().isBefore(event.getTimestamp().toInstant()) && !latest.get()
            .getState()
            .equals(event.getState()))) {
            SprinklerStatusEntity status = statusRepository.save(new SprinklerStatusEntity(Timestamp.from(event.getTimestamp().toInstant()), event.getState()));
            logger.debug("updateSprinkler:status={}", status);
        }
    }

    @Override
    public DateRange findDateRange() {
        ZonedDateTime start = weatherDataRepository.findFirstDataTime();
        ZonedDateTime end = weatherDataRepository.findLastDataTime();
        logger.debug("findDateRange:{}:{}", start, end);
        return new DateRange(start, end);
    }

    @Override
    public List<WeatherData> listAllWeather() {
        return weatherDataRepository.findAll()
            .stream()
            .map(WeatherDataEntity::toInfo)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<WeatherData> latestWeather(ZonedDateTime timestamp) {
        return weatherDataRepository.findLatestFor(Timestamp.from(timestamp.toInstant()))
            .map(WeatherDataEntity::toInfo);
    }

    @Override
    public Double rainMeasuredFor(DateRange range) {
        Assert.isTrue(range.getStart().isBefore(range.getEnd()) || range.getStart().equals(range.getEnd()),
            () -> "Expected " + range.getStart() + " <= " + range.getEnd());
        List<WeatherDataEntity> latest = weatherDataRepository.findLatestForRange(Timestamp.from(range.getStart().toInstant()),
            Timestamp.from(range.getEnd().toInstant()));
        logger.debug("rainMeasuredFor:{}:{}", range, latest);
        return latest
            .stream()
            .map(WeatherDataEntity::getRainMeasured)
            .filter(Objects::nonNull)
            .reduce(Double::sum).orElse(0.0);
    }

    @Override
    public List<SprinklerStatus> listAllStatus() {
        return StreamSupport.stream(statusRepository.findAll().spliterator(), false).map(SprinklerStatusEntity::toInfo).collect(Collectors.toList());
    }
}
