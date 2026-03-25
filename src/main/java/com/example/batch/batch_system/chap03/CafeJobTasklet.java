package com.example.batch.batch_system.chap03;

import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CafeJobTasklet implements Tasklet {

    private int cakeCount = 0;
    private final int ORDER_TARGET = 5;

    @Override
    public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
            throws Exception {

        cakeCount++;

        log.info("케이크를 만들고 있습니다. ({}/{})", cakeCount, ORDER_TARGET);

        if (cakeCount >= ORDER_TARGET) {
            log.info("케이크 만들기 완료");
            return RepeatStatus.FINISHED;
        }

        return RepeatStatus.CONTINUABLE;
    }

}
