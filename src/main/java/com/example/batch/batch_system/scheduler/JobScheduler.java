package com.example.batch.batch_system.scheduler;

import java.time.LocalDate;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.parameters.InvalidJobParametersException;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.launch.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobScheduler {

    private final JobOperator jobOperator;
    private final Job settlementJob;


    @Scheduled(cron = "0 0 4 * * *")
    public void runJob()
            throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException,
            InvalidJobParametersException, JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("targetDate", LocalDate.now().minusDays(7).toString())
                .addLong("time", System.currentTimeMillis()).toJobParameters();

        log.info("스케줄러 작동 ! 배치 실행");

        jobOperator.start(settlementJob, jobParameters);
    }

}
