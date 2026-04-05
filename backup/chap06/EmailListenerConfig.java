package com.example.batch.batch_system.chap06;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// ./gradlew bootRun --args='--spring.batch.job.name=emailJob'
@Configuration
@Slf4j
@RequiredArgsConstructor
public class EmailListenerConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final JobReportListener jobReportListener;

    @Bean
    public Job emailJob() {
        return new JobBuilder("emailJob", jobRepository).start(emailStep())
                .listener(jobReportListener).build();
    }

    @Bean
    public Step emailStep() {
        return new StepBuilder("emailStep", jobRepository)
                .tasklet(((stepContribution, chunkContext) -> {
                    log.info("3 초 뒤 에러를 발생시킵니다.");

                    Thread.sleep(3000);

                    throw new RuntimeException("테스트를 위한 에러 발생");
                }), transactionManager).build();
    }
}
