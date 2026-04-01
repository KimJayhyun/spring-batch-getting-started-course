package com.example.batch.batch_system.chap03;

import java.io.File;
import java.time.LocalDate;

import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class FileCleanupTasklet implements Tasklet {

    private final String rootPath;
    private final int retentionDays;

    @Override
    public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
            throws Exception {

        LocalDate cutoffDate = LocalDate.now().minusDays(retentionDays);

        File folder = new File(rootPath);
        File[] files = folder.listFiles();

        if (files == null) {
            return RepeatStatus.FINISHED;
        }

        for (File file : files) {
            String name = file.getName();

            if (name.endsWith(".log") && name.length() >= 10) {
                // access_2026-01-26.log -> "2026-01-26" 부분 추출
                try {
                    String dateStr =
                            name.substring(name.lastIndexOf("_") + 1, name.lastIndexOf("."));
                    LocalDate fileDate = LocalDate.parse(dateStr); // 기본 ISO_LOCAL_DATE(yyyy-MM-dd)

                    if (fileDate.isBefore(cutoffDate)) {
                        file.delete();
                        log.info("삭제된 로그: {}", name);
                    }
                } catch (Exception e) {
                    // 날짜 형식이 아니거나 파싱 불가능한 파일(system_config.conf 등)은 자연스럽게 스킵
                    // continue;
                }
            }

        }

        return RepeatStatus.FINISHED;
    }

}
