package com.example.sprinkler.common;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface SprinklerStatusRepository extends Repository<SprinklerStatus, Long> {
	@Query("from SprinklerStatus where statusTime = max(statusTime)")
	Optional<SprinklerStatus> findLatest();
}
