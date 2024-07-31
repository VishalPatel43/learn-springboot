package com.springboot.coding.introductionToSpringBoot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IntroductionToSpringBootApplication implements CommandLineRunner {

    @Autowired // this annotation is used to inject the object of the class
    // inject the dependency of Apple inside Spring boot Bean
    // field injection
    Apple obj;

    public static void main(String[] args) {

        SpringApplication.run(IntroductionToSpringBootApplication.class, args);
    }

    // non-static method we can use the coz we don't want to create the object of the class
    @Override
    public void run(String... args) throws Exception {
        obj.eatApple();

    }
}
