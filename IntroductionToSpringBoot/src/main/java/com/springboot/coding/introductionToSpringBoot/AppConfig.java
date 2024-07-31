package com.springboot.coding.introductionToSpringBoot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// explicitly define the bean and configure
@Configuration
public class AppConfig {

    // creation of object part is done by the developer not by the spring
    // but DI is done by the spring
    @Bean
    Apple getApple() { // define the factory method to create the object of the class
        return new Apple();
    }
}
