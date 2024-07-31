package com.springboot.coding.introductionToSpringBoot;

import org.springframework.stereotype.Component;

// @Component // this annotation is used to create the object of the class,
// notified to the spring that this is the class which is going to be used as a bean
public class Apple {

    void eatApple() {
        System.out.println("I am eating the apple");
    }
}
