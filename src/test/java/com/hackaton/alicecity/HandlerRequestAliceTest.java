package com.hackaton.alicecity;

import com.hackaton.alicecity.common.MessageFromResource;
import com.hackaton.alicecity.common.exception.ExceptionApp;
import com.hackaton.alicecity.dto.*;
import com.hackaton.alicecity.entity.User;
import com.hackaton.alicecity.repository.QuizRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
public class HandlerRequestAliceTest {
    /*
    @Autowired
    HandlerRequestAlice handlerRequestAlice;
    @Autowired
    MessageFromResource message;
    @Autowired
    QuizRepository quizRepository;

    @Test
    @SneakyThrows
    void testSessionSaveWithNotUser() {
        AliceRequest aliceRequest = new AliceRequest(new Request(TypeRequest.SimpleUtterance, new Nlu(List.of())), new Session("test"));
        assertThrows(ExceptionApp.class, () -> {
            handlerRequestAlice.handle(aliceRequest);
        });
    }

    @Test
    @SneakyThrows
    void testCorrectSessionSave() {
        Session session = new Session("test");
        session.setUser(new User("test_id"));
        AliceRequest aliceRequest = new AliceRequest(new Request(TypeRequest.SimpleUtterance, new Nlu(List.of())), session);
        assertDoesNotThrow(() -> {
            handlerRequestAlice.handle(aliceRequest);
        });
    }

    @Test
    @SneakyThrows
    void testFirstMessage() {
        Session session = new Session("test2");
        session.setUser(new User("test_id2"));
        AliceRequest aliceRequest = new AliceRequest(new Request(TypeRequest.SimpleUtterance, new Nlu(List.of())), session);
        AliceResponse response = handlerRequestAlice.handle(aliceRequest);
        assertEquals(response.getResponse().getText(), message.getGreetingStartFirstTime(), "");
    }

    @Test
    @SneakyThrows
    void testGetQuiz() {
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 10; i++) {
                Thread.sleep(10000);//DDoS-атаки
                Result d = quizRepository.getQuestion();
                log.info(d.isCorrectAnswer()+"answer");
            }
        });

    }
*/
}
