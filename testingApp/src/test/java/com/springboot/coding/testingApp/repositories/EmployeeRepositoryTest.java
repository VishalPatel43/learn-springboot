package com.springboot.coding.testingApp.repositories;

import com.springboot.coding.testingApp.TestContainerConfiguration;
import com.springboot.coding.testingApp.entities.Employee;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest // load entire spring context --> web server, database, etc.
//DataJpaTest, SpringBootTest --> so we can use the @Autowired annotation for bean
@DataJpaTest
// load only JPA context, database related context --> Load the H2 database default and don't use Postgres database
// @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // it will replace the database with H2 and don't take credentials for postgres database    // it will replace the database with H2 and don't take credentials for postgres database
 @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)  // Not load the H2 database or otherwise remove the H2 database from pom file
@Slf4j
@Import(TestContainerConfiguration.class)
class EmployeeRepositoryTest {

    // @DataJpaTest After each test method, the database is rolled back to its previous state

    // Inject What we are testing
    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
//         --> it will run before each test method
    void setUp() {
        employee = Employee.builder()
                .employeeId(1L)
                .name("Vishal")
                .email("v@email.com")
                .salary(200_000L)
                .build();
    }

    // 3 Things when we write the test --> Arrange (Given), Act (When), Assert (Then)
    @Test
    void testFindByEmail_whenEmailIsPresent_thenReturnEmployee() {

        // Arrange (Given) --> Arrange the test data
        // Create an Employee then we find the employee by email
        employeeRepository.save(employee);

        // Act (When)
        Optional<Employee> getEmployee = employeeRepository.findByEmail(employee.getEmail());

        log.info("Employee: {}", getEmployee);
        // Assert (Then)
        assertTrue(getEmployee.isPresent(), "Employee should present");
//        assertTrue(getEmployee.isPresent());
        assertEquals(employee.getEmail(), getEmployee.get().getEmail());
        getEmployee.ifPresent(e -> {
            assertEquals(employee.getEmail(), e.getEmail());
        });
    }

    @Test
    void testFindAll_whenEmployeeIsSaved_thenReturnEmployeeList() {
        // Arrange
        employeeRepository.save(employee); // just add 1

        // Act
        List<Employee> employeeList = getAllEmployees();

        // Assert
        assertThat(employeeList).isNotNull();
        assertThat(employeeList).isNotEmpty();
        assertEquals(1, employeeList.size());
        assertEquals(employee.getEmail(), employeeList.getFirst().getEmail());
    }

    @Test
    void testFindByEmail_whenEmailIsNotFound_thenReturnEmptyOptionalEmployee() {
        // Given (Arrange)
        String email = "notPresent.123@gmail.com";

        // When (Act)
        Optional<Employee> employee = employeeRepository.findByEmail(email);
        log.info("Employee : {}", employee);

        // Then (Assert)
        assertThat(employee).isNotNull();
        assertThat(employee).isEmpty();
    }

    @Test
    void testFindAll_whenNoEmployeesPresent_thenReturnEmptyList() {
        // Act
        List<Employee> employeeList = getAllEmployees();

        // Then
        assertThat(employeeList).isNotNull(); // The list should not be null
//        assertThat(employeeList).isNotEmpty();
        assertThat(employeeList).isEmpty();   // The list should be empty
    }

    private List<Employee> getAllEmployees() {
        List<Employee> employeeList = employeeRepository.findAll();
        log.info("Size of Employee List: {}", employeeList.size());
        log.info("Employee List: {}", employeeList);
        return employeeList;
    }
}