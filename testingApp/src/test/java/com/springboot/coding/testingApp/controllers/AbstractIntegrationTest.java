package com.springboot.coding.testingApp.controllers;


import com.springboot.coding.testingApp.TestContainerConfiguration;
import com.springboot.coding.testingApp.dto.EmployeeDTO;
import com.springboot.coding.testingApp.entities.Employee;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

// WebTestClient not created coz we are not dealing with the real server, it is just a mock server
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// for that we define the webEnvironment as RANDOM_PORT
@AutoConfigureWebTestClient(timeout = "100000") // to increase the timeout --> 100sec
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)  // use with @DataJpaTest
@Import(TestContainerConfiguration.class)
public class AbstractIntegrationTest {

    @Autowired
    WebTestClient webTestClient;

    Employee testEmployee = Employee.builder()
            .employeeId(1L)
            .name("Vishal")
            .email("v@email.com")
            .salary(200_000L)
            .build();

    EmployeeDTO testEmployeeDTO = EmployeeDTO.builder()
            .employeeId(1L)
            .name("Vishal")
            .email("v@email.com")
            .salary(200_000L)
            .build();

}
