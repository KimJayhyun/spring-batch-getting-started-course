package com.example.batch.batch_system.job;

import java.time.LocalDate;
import java.util.Collections;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.batch.infrastructure.item.database.JpaPagingItemReader;
import org.springframework.batch.infrastructure.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.infrastructure.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.batch.batch_system.domain.Orders;
import com.example.batch.batch_system.domain.Settlement;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class SettlementJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job settlementJob() {
        return new JobBuilder("settlementJob", jobRepository).start(settlementStep()).build();
    }

    @Bean
    public Step settlementStep() {
        return new StepBuilder("settlementStep", jobRepository).<Orders, Settlement>chunk(1000)
                .transactionManager(transactionManager).reader(ordersReader(null))
                .processor(settlementProcessor()).writer(settlementWriter()).build();
    }

    // Item Reader
    @Bean
    @StepScope
    public JpaPagingItemReader<Orders> ordersReader(
            @Value("#{jobParameters['targetDate']}") String targetDate) {

        log.info("[Reader] 정산 집계 대상 날짜: {}", targetDate);

        return new JpaPagingItemReaderBuilder<Orders>().name("ordersReader")
                .entityManagerFactory(entityManagerFactory).pageSize(1000)
                .queryString("SELECT o FROM Orders o WHERE o.orderDate = :targetDate ORDER BY o.id")
                .parameterValues(
                        Collections.singletonMap("targetDate", LocalDate.parse(targetDate)))
                .build();

    }

    // Item Processor
    @Bean
    @StepScope
    public ItemProcessor<Orders, Settlement> settlementProcessor() {
        return item -> {
            int fee = (int) (item.getAmount() * 0.3);
            int settlementAmount = item.getAmount() - fee;

            return new Settlement(item.getId(), item.getStoreName(), settlementAmount,
                    LocalDate.now());

        };
    }

    // Item Writer
    @Bean
    public JpaItemWriter<Settlement> settlementWriter() {
        return new JpaItemWriterBuilder<Settlement>().entityManagerFactory(entityManagerFactory)
                .build();
    }
}
