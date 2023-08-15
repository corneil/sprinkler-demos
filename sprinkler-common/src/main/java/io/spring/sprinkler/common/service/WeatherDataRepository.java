package io.spring.sprinkler.common.service;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface WeatherDataRepository extends Repository<WeatherDataEntity, ZonedDateTime> {
    @Query("select * from WEATHER_DATA where WEATHER_TIME between :start and :end")
    List<WeatherDataEntity> findLatestForRange(@Param("start") Timestamp start, @Param("end") Timestamp end);

    @Query("select * from WEATHER_DATA where WEATHER_TIME = (select max(WEATHER_TIME) from WEATHER_DATA where WEATHER_TIME <= :ts)")
    Optional<WeatherDataEntity> findLatestFor(@Param("ts") Timestamp timestamp);
    @Query("select min(WEATHER_TIME) from WEATHER_DATA")
    ZonedDateTime findFirstDataTime();
    @Query("select max(WEATHER_TIME) from WEATHER_DATA")
    ZonedDateTime findLastDataTime();

    List<WeatherDataEntity> findAll();
}
