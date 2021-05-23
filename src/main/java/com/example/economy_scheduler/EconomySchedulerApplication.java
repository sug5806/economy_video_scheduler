package com.example.economy_scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EconomySchedulerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EconomySchedulerApplication.class, args);
    }

}
