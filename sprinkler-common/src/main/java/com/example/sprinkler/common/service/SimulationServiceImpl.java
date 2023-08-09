package com.example.sprinkler.common.service;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.sprinkler.common.DateRange;
import com.example.sprinkler.common.SimulationService;
import com.example.sprinkler.common.SprinklerEvent;
import com.example.sprinkler.common.SprinklerStatus;
import com.example.sprinkler.common.WeatherData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        Optional<SprinklerStatus> sprinklerStatus = statusRepository.findLatest(timestamp).map(SprinklerStatusEntity::toInfo);
        logger.debug("findLatestStatus:{}", sprinklerStatus.orElse(null));
        return sprinklerStatus;
    }

    @Override
    public void updateSprinkler(SprinklerEvent event) {
        Optional<SprinklerStatusEntity> latest = statusRepository.findLatest(event.timestamp());
        logger.debug("updateSprinkler:latest={}", latest.orElse(null));
        if (latest.isEmpty() || (latest.get().getStatusTime().toInstant().isBefore(event.timestamp().toInstant()) && !latest.get()
            .getState()
            .equals(event.state()))) {
            SprinklerStatusEntity status = statusRepository.save(new SprinklerStatusEntity(null, Timestamp.from(event.timestamp().toInstant()), event.state()));
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
        Assert.isTrue(range.start().isBefore(range.end()) || range.start().equals(range.end()), ()-> "Expected " + range.start() + " <= " + range.end());
        List<WeatherDataEntity> latest = weatherDataRepository.findLatestForRange(Timestamp.from(range.start().toInstant()), Timestamp.from(range.end().toInstant()));
        logger.debug("rainMeasuredFor:{}:{}", range, latest);
        return latest
            .stream()
            .map(WeatherDataEntity::getRainMeasured)
            .filter(Objects::nonNull)
            .reduce(Double::sum).orElse(0.0);
    }
}
