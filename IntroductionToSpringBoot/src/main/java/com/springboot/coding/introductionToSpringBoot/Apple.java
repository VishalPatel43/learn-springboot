package com.springboot.coding.introductionToSpringBoot;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

// @Component // this annotation is used to create the object of the class,
// notified to the spring that this is the class which is going to be used as a bean
public class Apple {

    void eatApple() {
        System.out.println("I am eating the apple");
    }

    // don't call this method explicitly it will be called by the IOC container
    @PostConstruct
    void callThisBeforeAppleIsUsed() {
        System.out.println("Creating the apple before use");
    }

    // don't call this method explicitly it will be called by the IOC container
    @PreDestroy
    void callThisBeforeDestroy() {
        System.out.println("Destroying the Apple bean");
    }
}
