package io.spring.sprinkler.common.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import io.spring.sprinkler.common.DateRange;
import io.spring.sprinkler.common.SimulationService;
import io.spring.sprinkler.common.SprinklerEvent;
import io.spring.sprinkler.common.SprinklerStatus;
import io.spring.sprinkler.common.WeatherData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

public class SimulationServiceImpl implements SimulationService {
    private static final Logger logger = LoggerFactory.getLogger(SimulationServiceImpl.class);

    private final SprinklerStatusRepository statusRepository;

    private final WeatherDataRepository weatherDataRepository;

    private final JdbcTemplate jdbcTemplate;

    public SimulationServiceImpl(SprinklerStatusRepository statusRepository, WeatherDataRepository weatherDataRepository, JdbcTemplate jdbcTemplate) {
        this.statusRepository = statusRepository;
        this.weatherDataRepository = weatherDataRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void resetState() {
        this.statusRepository.deleteByIdGreaterThan(1L);
        this.jdbcTemplate.execute("alter sequence SPRINKLER_STATE_SEQ restart with 2");
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SprinklerStatus> findLatestStatus(@NonNull ZonedDateTime timestamp) {
        Assert.notNull(timestamp, "timestamp required");
        Optional<SprinklerStatus> sprinklerStatus = statusRepository.findLatestFor(Timestamp.from(timestamp.toInstant()))
            .map(SprinklerStatusEntity::toInfo);
        logger.debug("findLatestStatus:{}", sprinklerStatus);
        return sprinklerStatus;
    }

    @Override
    @Transactional
    public void updateSprinkler(@NonNull SprinklerEvent event) {
        Assert.notNull(event, "event required");
        logger.info("event:{}", event);
        Instant eventTimestamp = event.getTimestamp().toInstant();
        long count = statusRepository.countByStatusTimeGreaterThan(Timestamp.from(eventTimestamp));
        if (count > 0) {
            logger.info("deleting:{} items after:{}", count, eventTimestamp);
            statusRepository.deleteByStatusTimeGreaterThan(Timestamp.from(eventTimestamp));
        }
        Optional<SprinklerStatusEntity> latest = statusRepository.findLatestFor(Timestamp.from(eventTimestamp));
        if (latest.isEmpty() || !latest.get().getState().equals(event.getState())) {
            logger.info("updateSprinkler:latest={}", latest.orElse(null));
            SprinklerStatusEntity status = statusRepository.save(new SprinklerStatusEntity(Timestamp.from(eventTimestamp), event.getState()));
            logger.info("updateSprinkler:status={}", status);
        } else {
            logger.info("updateSprinkler:skipped:latest={}", latest.orElse(null));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public DateRange findDateRange() {
        ZonedDateTime start = weatherDataRepository.findFirstDataTime();
        ZonedDateTime end = weatherDataRepository.findLastDataTime();
        logger.debug("findDateRange:{}:{}", start, end);
        return new DateRange(start, end);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WeatherData> listAllWeather() {
        return weatherDataRepository.findAll()
            .stream()
            .map(WeatherDataEntity::toInfo)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WeatherData> latestWeather(ZonedDateTime timestamp) {
        return weatherDataRepository.findLatestFor(Timestamp.from(timestamp.toInstant()))
            .map(WeatherDataEntity::toInfo);
    }

    @Override
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public List<SprinklerStatus> listAllStatus() {
        return StreamSupport.stream(statusRepository.findAll().spliterator(), false).map(SprinklerStatusEntity::toInfo).collect(Collectors.toList());
    }
}
