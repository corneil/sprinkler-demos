package com.example.sprinkler.common.service;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.relational.core.mapping.event.BeforeConvertCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

public class SprinklerStatusIdGenerator implements BeforeConvertCallback<SprinklerStatusEntity> {

    private final static Logger log = LoggerFactory.getLogger(SprinklerStatusIdGenerator.class);

    private final JdbcTemplate jdbcTemplate;

    public SprinklerStatusIdGenerator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public SprinklerStatusEntity onBeforeConvert(SprinklerStatusEntity entity) {
        if (entity.getId() == null) {
            Long id = jdbcTemplate.queryForObject("select nextval('SPRINKLER_STATE_SEQ')", Long.TYPE);
            log.info("generated id:{}", id);
            Assert.notNull(id, "Expected id from SPRINKLER_STATE_SEQ");
            entity.setId(id);
        }
        return entity;
    }
}
