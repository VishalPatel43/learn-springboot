package com.springboot.coding.introductionToSpringBoot.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

// explicitly define the bean and configure
@Configuration
public class AppConfig {

    // creation of object part is done by the developer not by the spring
    // but DI is done by the spring

    // default is singleton scope, we use 90% of the time
    @Bean // if we use Apple or not but the object will be created
    @Scope("singleton")
    // define the scope of the object (default is singleton), create the object only once
//    @Scope("prototype") // create the new object every time
    Apple getApple() { // define the factory method to create the object of the class
        return new Apple();
    }
}
