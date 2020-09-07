package com.tom.tz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.tom.tz"})
public class RocketWithESApplication {

    public static void main(String[] args) {

        SpringApplication.run(RocketWithESApplication.class, args);
    }
}
