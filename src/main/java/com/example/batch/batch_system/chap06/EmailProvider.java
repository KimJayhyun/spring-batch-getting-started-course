package com.example.batch.batch_system.chap06;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EmailProvider {

    public void send(String to, String subject, String message) {
        // 실제 SMTP 사용
        log.info("[메일 발송 성공] 받는 사람: {}", to);
        log.info("subject: {}", subject);

    }

}
