package com.springboot.coding.introductionToSpringBoot.di;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;


// Priority
// Environment variable which set in application.properties <-- application.properties <-- @Primary

//@Primary // when we have more than one implementation of an interface, we can use @Primary to specify which implementation should be used by default.
// otherwise it will give ambiguous error
@Component
@ConditionalOnProperty(name = "deploy.env", havingValue = "production")
public class ProDB implements DB {

    @Override
    public String getData() {
        return "Production Data";
    }
}
