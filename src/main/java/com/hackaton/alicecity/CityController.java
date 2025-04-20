package com.hackaton.alicecity;

import com.hackaton.alicecity.common.exception.ExceptionApp;
import com.hackaton.alicecity.dto.AliceRequest;
import com.hackaton.alicecity.dto.AliceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/city")
@Slf4j
public class CityController {
    private final HandlerRequestAlice handlerRequestAlice;

    CityController(HandlerRequestAlice handlerRequestAlice) {
        this.handlerRequestAlice = handlerRequestAlice;
    }

    @PostMapping
    public AliceResponse talkYandexAlice(@RequestBody AliceRequest aliceRequest) throws ExceptionApp {
        return handlerRequestAlice.handle(aliceRequest);
    }
}
