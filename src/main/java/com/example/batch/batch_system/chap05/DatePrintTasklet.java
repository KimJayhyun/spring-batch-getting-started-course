package com.example.batch.batch_system.chap05;

import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@StepScope
@Component
@Slf4j
public class DatePrintTasklet implements Tasklet {

    private final String requestDate;

    public DatePrintTasklet(@Value("#{jobParameters['requestDate']}") String requestDate) {
        this.requestDate = requestDate;
    }

    @Override
    public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
            throws Exception {
        log.info("외부에서 받은 날짜: {}", requestDate);
        log.info("이제 {} 날짜의 데이터를 처리합니다.", requestDate);

        return null;
    }

}
