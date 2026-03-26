package com.example.batch.batch_system.chap04;

import java.util.Arrays;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// ./gradlew bootRun --args='--spring.batch.job.name=simpleJob'
@Configuration
@RequiredArgsConstructor
@Slf4j
public class SimpleChunkConfig {

    private final JobRepository jobRepository;
    // private final PlatformTransactionManager transactionManager;

    @Bean
    public Job simpleJob() {
        return new JobBuilder("simpleJob", jobRepository).start(simpleStep()).build();
    }

    @Bean
    public Step simpleStep() {
        return new StepBuilder("simpleStep", jobRepository).<String, String>chunk(2)
                .reader(itemReader()).processor(itemProcessor()).writer(itemWriter()).build();
    }

    // 1. ItemReader
    @Bean
    public ItemReader<String> itemReader() {
        return new ListItemReader<>(
                Arrays.asList("ice americano", "latte", "mocha", "cappuccino", "espresso"));
    }

    // 2. ItemProcessor
    @Bean
    public ItemProcessor<String, String> itemProcessor() {
        return item -> item.toUpperCase();
    }

    // 3. ItemWriter
    @Bean
    public ItemWriter<String> itemWriter() {
        return items -> {
            log.info("-- 청크 쓰기 시작");

            for (String item : items) {
                log.info("결과 {}", item);
            }

            log.info("-- 청크 쓰기 완료");
        };
    }

}
