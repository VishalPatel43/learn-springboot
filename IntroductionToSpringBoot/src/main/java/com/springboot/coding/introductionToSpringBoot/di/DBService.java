package com.springboot.coding.introductionToSpringBoot.di;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service // This annotation is used to mark the class as a service class.
// @Service is a specialization of the @Component annotation with the purpose of representing the role of a service in the application.
public class DBService {

    //    @Autowired
    private final DB db;

    @Autowired
    public DBService(DB db) {
        this.db = db;
    }

    public String getData() {
        return db.getData();
    }
}
