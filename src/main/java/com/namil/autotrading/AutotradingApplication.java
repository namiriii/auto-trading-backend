package com.namil.autotrading;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AutotradingApplication {

    public static void main(String[] args) {

        SpringApplication.run(AutotradingApplication.class, args);

    }


}
