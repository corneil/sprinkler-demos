package com.example.sprinkler.common.service;

import javax.sql.DataSource;

import com.example.sprinkler.common.SimulationService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJdbcRepositories
@Profile("server")
public class SprinklerServiceConfig {
    @Bean
    NamedParameterJdbcOperations namedParameterJdbcOperations(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public SimulationService simulationService(SprinklerStatusRepository statusRepository, WeatherDataRepository weatherDataRepository) {
        return new SimulationServiceImpl(statusRepository, weatherDataRepository);
    }

    @Bean
    public SprinklerStatusIdGenerator sprinklerStatusIdGenerator(DataSource dataSource) {
        return new SprinklerStatusIdGenerator(new JdbcTemplate(dataSource));
    }
}
