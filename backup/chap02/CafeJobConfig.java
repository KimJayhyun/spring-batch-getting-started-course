package com.example.batch.batch_system.chap02;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class CafeJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    private int coffeeCount = 0;
    private final int ORDER_TARGET = 5;

    @Bean
    public Job cafeJob() {
        return new JobBuilder("cafeJob", jobRepository).start(openCaStep()).next(makeCoffeeStep())
                .next(closeCafeStep()).build();
    }


    // 1. 카페 문 열기 -> openCafeStep
    @Bean
    public Step openCaStep() {
        return new StepBuilder("openCafeStop", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("[오픈] 카페 문 열기");
                    return RepeatStatus.FINISHED;
                }, transactionManager).build();
    }

    // 2. 커피 만들기(5 잔) -> makeCoffeeStep
    @Bean
    public Step makeCoffeeStep() {
        return new StepBuilder("makeCoffeeStep", jobRepository)
                .tasklet(((stepContribution, chunkContext) -> {
                    coffeeCount++;
                    System.out.println("[커피 만들기] " + coffeeCount + "번째 카페 만들기");

                    if (coffeeCount < ORDER_TARGET) {
                        return RepeatStatus.CONTINUABLE;
                    } else {
                        System.out.println("[커피 만들기] 카페 만들기 완료");
                        return RepeatStatus.FINISHED;
                    }
                }), transactionManager).build();
    }

    // 3. 마감 청소 및 퇴근 -> closeCafeStep
    @Bean
    public Step closeCafeStep() {
        return new StepBuilder("closeCafeStep", jobRepository)
                .tasklet(((stepContribution, chunkContext) -> {
                    System.out.println("[마감 청소 및 퇴근]");
                    return RepeatStatus.FINISHED;
                }), transactionManager).build();

    }

}
