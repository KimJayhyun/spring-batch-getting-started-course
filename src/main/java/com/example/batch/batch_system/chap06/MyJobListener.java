package com.example.batch.batch_system.chap06;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MyJobListener implements JobExecutionListener {


    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("[job 시작] id: {}", jobExecution.getJobInstanceId());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.FAILED) {
            log.warn("[job 실패] 관리자에게 연락하세요!");
        } else {
            log.info("[job 성공] 정상적으로 job이 종료되었습니다.");
        }
    }
}
