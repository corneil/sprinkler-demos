package io.spring.sprinkler.common.service;

import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface SprinklerStatusRepository extends CrudRepository<SprinklerStatusEntity, Long> {
    @Query("delete from SPRINKLER_STATE where STATUS_TIME > :ts")
    @Modifying
    void deleteByStatusTimeGreaterThan(@Param("ts") Timestamp statusTime);
    long countByStatusTimeGreaterThan(Timestamp statusTime);
    @Query("delete from SPRINKLER_STATE where ID > :id")
    @Modifying
	void deleteByIdGreaterThan(@Param("id") Long id);
	@Query("select ID,STATUS_TIME,STATE from SPRINKLER_STATE where STATUS_TIME = (select max(STATUS_TIME) from SPRINKLER_STATE where STATUS_TIME <= :ts)")
	Optional<SprinklerStatusEntity> findLatestFor(@Param("ts") Timestamp timestamp);
}
