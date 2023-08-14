package io.spring.sprinkler;

import io.spring.sprinkler.entity.SprinklerStatus;
import io.spring.sprinkler.entity.WeatherData;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@EnableTask
@Configuration
public class SprinklerReportConfiguration {

    @Bean
    public Job sprinklerReportJob(JobRepository jobRepository, Step step1) {
        return new JobBuilder("sprinkler report job", jobRepository)
                .start(step1)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step sprinklerReportStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                                    ItemReader<WeatherData> reader, ItemProcessor<WeatherData, SprinklerStatus> itemProcessor,
                                    ItemWriter<SprinklerStatus> writer) {
        return new StepBuilder("sprinkler report step", jobRepository).<WeatherData, SprinklerStatus>chunk(5, transactionManager).
                reader(reader).
                processor(itemProcessor).
                writer(writer).
                build();
    }

    @Bean
    public JdbcCursorItemReader<WeatherData> weatherDataTableReader(DataSource dataSource) {
        String sql = "select * from weather_data";
        return new JdbcCursorItemReaderBuilder<WeatherData>()
                .name("SprinklerDataTableReader")
                .dataSource(dataSource)
                .sql(sql)
                .rowMapper(new DataClassRowMapper<>(WeatherData.class))
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<SprinklerStatus> billingDataTableWriter(DataSource dataSource) {
        String sql = "insert into sprinkler_report (status_date, state) values ( :statusDate, :state)";
        return new JdbcBatchItemWriterBuilder<SprinklerStatus>()
                .dataSource(dataSource)
                .sql(sql)
                .beanMapped()
                .build();
    }

    @Bean
    public ItemProcessor<WeatherData, SprinklerStatus> itemProcessor(JdbcTemplate jdbcTemplate) {
        return item -> {
            String sql = "SELECT state FROM sprinkler_state WHERE statusTime = ?";
            String sprinklerStateForDay = "Sprinkler was activated.";
            try {
                Integer i = jdbcTemplate.queryForObject(sql, Integer.class, item.weatherTime());
                if (i.equals(0)) {
                    sprinklerStateForDay = "Sprinkler was not activated.";
                }
            } catch (EmptyResultDataAccessException erd) {
                sprinklerStateForDay = "Sprinkler did not report activity.";
            }

            return new SprinklerStatus(item.weatherTime(), sprinklerStateForDay);
        };
    }


    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
    
    

}
