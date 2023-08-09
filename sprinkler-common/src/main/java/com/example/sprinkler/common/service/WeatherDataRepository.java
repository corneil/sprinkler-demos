package com.example.sprinkler.common.service;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Pair;

public interface WeatherDataRepository extends Repository<WeatherDataEntity, ZonedDateTime> {
    @Query("select * from weather_data where weather_time between :start and :end")
    List<WeatherDataEntity> findLatestForRange(@Param("start") Timestamp start, @Param("end") Timestamp end);

    @Query("select * from weather_data where weather_time = (select max(weather_time) from weather_data where weather_time <= :ts)")
    Optional<WeatherDataEntity> findLatestFor(@Param("ts") Timestamp timestamp);
    @Query("select min(weather_time) from weather_data")
    ZonedDateTime findFirstDataTime();
    @Query("select max(weather_time) from weather_data")
    ZonedDateTime findLastDataTime();

    List<WeatherDataEntity> findAll();
}
