package com.springboot.springbootwebtutorial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootWebTutorialApplication {

//	Access database
//	jdbc:h2:file:/Users/vishal/Technical/SpringBoot/codeshuttle/SpringBoot/code/springbootwebtutorial
//	username: vishalDB
//	password: Vishal123
//	localhost:8080/h2-console

	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebTutorialApplication.class, args);
	}

}
