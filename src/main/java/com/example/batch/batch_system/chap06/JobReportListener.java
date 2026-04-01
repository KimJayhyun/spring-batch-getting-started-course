package com.example.batch.batch_system.chap06;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobReportListener {

    private final EmailProvider emailProvider;

    @BeforeJob
    public void before(JobExecution jobExecution) {
        log.info("배치를 시작합니다: {}", jobExecution.getJobInstanceId());

    }

    @AfterJob
    public void after(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.FAILED) {
            emailProvider.send("admin@naver.com", "Job Report",
                    "job id: " + jobExecution.getJobInstanceId() + " Failed");
        } else {
            log.info("배치 완료: {}", jobExecution.getJobInstanceId());
        }
    }

}
