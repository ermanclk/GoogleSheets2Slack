package com.crossover.sheetstoslack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.security.GeneralSecurityException;

@SpringBootApplication
@EnableScheduling
public class SheetsSlackNotifierApplication {

    public static void main(String[] args) throws IOException, GeneralSecurityException {
        SpringApplication.run(SheetsSlackNotifierApplication.class, args);
    }
}

