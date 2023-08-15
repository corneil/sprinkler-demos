package io.spring.sprinkler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Testcontainers
class SprinklerReportApplicationTests {

    @Autowired
    DataSource dataSource;
    @Container
    static MariaDBContainer<?> mariaDBContainer = new MariaDBContainer<>("mariadb:latest");
	@Test
	void contextLoads() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(state) FROM sprinkler_report", Integer.class);
        assertThat(count).isEqualTo(14);
        count = jdbcTemplate.queryForObject("SELECT COUNT(state) FROM sprinkler_report WHERE state = 'Sprinkler did not report activity.'", Integer.class);
        assertThat(count).isEqualTo(4);
        count = jdbcTemplate.queryForObject("SELECT COUNT(state) FROM sprinkler_report WHERE state = 'Sprinkler was not activated.'", Integer.class);
        assertThat(count).isEqualTo(6);
        count = jdbcTemplate.queryForObject("SELECT COUNT(state) FROM sprinkler_report WHERE state = 'Sprinkler was activated.'", Integer.class);
        assertThat(count).isEqualTo(4);
	}


    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.batch.jdbc.initialize-schema", new Supplier<Object>() {
            @Override
            public String get() {
                return "always";
            }
        });
        registry.add("spring.datasource.url", mariaDBContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mariaDBContainer::getUsername);
        registry.add("spring.datasource.password", mariaDBContainer::getPassword);

    }
}
