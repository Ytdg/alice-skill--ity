package com.hackaton.alicecity.conf;

import com.hackaton.alicecity.repository.SessionRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class ClearCashSession {
    @Autowired
    private SessionRepository sessionRepository;


    @Scheduled(fixedRate = 1800000) // 30 минут
    @Transactional
    public void cleanupExpiredSessions() {
        log.info("clear_session");
        sessionRepository.deleteSessionsBefore(LocalDateTime.now());
    }
}
