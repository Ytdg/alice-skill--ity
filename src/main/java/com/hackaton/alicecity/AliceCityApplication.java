package com.hackaton.alicecity;

import jakarta.persistence.Entity;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Locale;


@SpringBootApplication
public class AliceCityApplication {
    public static void main(String[] args) {
        SpringApplication.run(AliceCityApplication.class, args);

    }
}
