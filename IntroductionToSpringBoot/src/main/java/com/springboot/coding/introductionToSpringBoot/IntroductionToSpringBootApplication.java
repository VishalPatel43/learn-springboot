package com.springboot.coding.introductionToSpringBoot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IntroductionToSpringBootApplication {

    public static void main(String[] args) {

        Apple apple = new Apple();
        apple.eatApple();

        SpringApplication.run(IntroductionToSpringBootApplication.class, args);
    }

}
