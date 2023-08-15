package io.spring.sprinkler.common.service;

import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface SprinklerStatusRepository extends CrudRepository<SprinklerStatusEntity, Long> {
	void deleteByIdGreaterThan(Long id);
	@Query("select ID,STATUS_TIME,STATE from SPRINKLER_STATE where STATUS_TIME = (select max(STATUS_TIME) from SPRINKLER_STATE where STATUS_TIME <= :ts)")
	Optional<SprinklerStatusEntity> findLatestFor(@Param("ts") Timestamp timestamp);
}
