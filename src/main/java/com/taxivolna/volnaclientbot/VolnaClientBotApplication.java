package com.taxivolna.volnaclientbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VolnaClientBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(VolnaClientBotApplication.class, args);
    }

}
