package com.example.sprinkler.common.service;

import java.time.ZonedDateTime;
import java.time.ZonedDateTime;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface SprinklerStatusRepository extends CrudRepository<SprinklerStatusEntity, Long> {
	@Query("select * from sprinkler_state where status_time = (select max(status_time) from sprinler_state where status_time <= :ts)")
	Optional<SprinklerStatusEntity> findLatest(@Param("ts") ZonedDateTime timestamp);
}
