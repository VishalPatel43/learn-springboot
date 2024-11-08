package com.springboot.coding.testingApp.controllers;

import com.springboot.coding.testingApp.advice.ApiError;
import com.springboot.coding.testingApp.advice.ApiResponse;
import com.springboot.coding.testingApp.dto.EmployeeDTO;
import com.springboot.coding.testingApp.entities.Employee;
import com.springboot.coding.testingApp.repositories.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;


import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
// --> No need to use this annotation coz it will use the TestContainerConfiguration where we have already defined the database configuration
class EmployeeControllerTestIT extends AbstractIntegrationTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll(); // if we don't delete then create employee will fail again and again coz its already present
    }

    @Order(8)
    @Test
    void testGetEmployeeById_success() {
        Employee savedEmployee = employeeRepository.save(testEmployee);
       /* Method 1
        testEmployeeDTO = modelMapper.map(savedEmployee, EmployeeDTO.class); // in case change the id, if if there we other test case which might change the id coz of auto increment

        ApiResponse<EmployeeDTO> apiResponse = webTestClient.get()
                .uri("/employees/{employeeId}", savedEmployee.getEmployeeId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<ApiResponse<EmployeeDTO>>() {
                })
                .returnResult()
                .getResponseBody();

//        EmployeeDTO employeeDTOResponse = (apiResponse != null) ? (apiResponse.getData()) : null;
        assert apiResponse != null;
        EmployeeDTO employeeDTOResponse = apiResponse.getData();

        assertThat(employeeDTOResponse).isEqualTo(testEmployeeDTO);

        assertThat(savedEmployee.getEmployeeId()).isEqualTo(employeeDTOResponse.getEmployeeId());
        assertThat(savedEmployee.getEmail()).isEqualTo(employeeDTOResponse.getEmail());
        assertThat(savedEmployee.getName()).isEqualTo(employeeDTOResponse.getName());
        assertThat(savedEmployee.getSalary()).isEqualTo(employeeDTOResponse.getSalary());
*/

        webTestClient.get()
                .uri("/employees/{employeeId}", savedEmployee.getEmployeeId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data.employeeId").isEqualTo(savedEmployee.getEmployeeId()) // Compare Json data when employee DTO and when we store the data in the database
                .jsonPath("$.data.email").isEqualTo(savedEmployee.getEmail())
                .jsonPath("$.data.name").isEqualTo(savedEmployee.getName())
                .jsonPath("$.data.salary").isEqualTo(savedEmployee.getSalary());
    }


    @Order(1)
    @Test
    void testGetEmployeeById_Failure() {
        /*
        * Just check the status code
        webTestClient.get()
                .uri("/employees/1")
                .exchange()
                .expectStatus().isNotFound();
        */
        ApiResponse<ApiError> apiResponse = webTestClient.get()
                .uri("/employees/1")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(new ParameterizedTypeReference<ApiResponse<ApiError>>() {
                })
                .returnResult()
                .getResponseBody();

        assert apiResponse != null;
        ApiError apiError = apiResponse.getError();
        log.info("ApiError: {}", apiError);

        assertThat(apiError).isNotNull();
        assertThat(apiError.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(apiError.getMessage()).isEqualTo("Employee not found with ID: 1");
    }

    @Order(4)
    @Test
    void testCreateNewEmployee_whenEmployeeAlreadyExists_thenThrowException() {
        // we should reset the employeeId to null, coz we are creating a new employee
        testEmployee.setEmployeeId(null);
        Employee savedEmployee = employeeRepository.save(testEmployee); // we create the employee
/* short method just check the status code
        webTestClient.post()
                .uri("/employees")
                .bodyValue(testEmployeeDTO)
                .exchange()
                .expectStatus().is5xxServerError();
        */
        ApiResponse<ApiError> apiResponse = webTestClient.post()
                .uri("/employees") // again we create the same employee (same email)
                .bodyValue(testEmployeeDTO)
                .exchange()
//                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectStatus().is5xxServerError()
                .expectBody(new ParameterizedTypeReference<ApiResponse<ApiError>>() {
                })
                .returnResult()
                .getResponseBody();

        assert apiResponse != null;
        ApiError apiError = apiResponse.getError();
//        log.info("ApiError: {}", apiError);

        assertThat(apiError).isNotNull();
        assertThat(apiError.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(apiError.getMessage()).isEqualTo("Employee already exists with email: " + testEmployeeDTO.getEmail());
        assertThat(apiError).isEqualTo(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR,
                "Employee already exists with email: " + testEmployeeDTO.getEmail()));
    }

    @Order(2)
    @Test
    void testCreateNewEmployee_whenEmployeeDoesNotExists_thenCreateEmployee() {
        // we should reset the employeeId to null, coz we are creating a new employee
//        testEmployee.setEmployeeId(null);

        // Direct check the status code, email and name
        webTestClient.post()
                .uri("/employees")
                .bodyValue(testEmployeeDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.data.email").isEqualTo(testEmployeeDTO.getEmail()) // -->  JSON data after creating employee --> We are not serializing the data, jackson is not working here
                .jsonPath("$.data.name").isEqualTo(testEmployeeDTO.getName())
                .jsonPath("$.data.salary").isEqualTo(testEmployeeDTO.getSalary());


/*

        * Alternatively, We can retrieve and assert the entire response body as well:
        ApiResponse<EmployeeDTO> apiResponse = webTestClient.post()
                .uri("/employees")
                .bodyValue(testEmployeeDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(new ParameterizedTypeReference<ApiResponse<EmployeeDTO>>() { // we convert the data using jackson
                })
                .returnResult()
                .getResponseBody();

        assert apiResponse != null;
        EmployeeDTO createdEmployee = apiResponse.getData();

        // Assertions for the created employee object
        assertThat(createdEmployee).isNotNull();
        assertThat(createdEmployee.getEmail()).isEqualTo(testEmployeeDTO.getEmail());
        assertThat(createdEmployee.getName()).isEqualTo(testEmployeeDTO.getName());
        assertThat(createdEmployee.getSalary()).isEqualTo(testEmployeeDTO.getSalary());
        assertThat(createdEmployee).isEqualTo(testEmployeeDTO);
*/

    }

    @Order(7)
    @Test
    void testUpdateEmployee_whenEmployeeIsValid_thenUpdateEmployee() {
        // Set up an employee in the database
        Employee savedEmployee = employeeRepository.save(testEmployee);

        testEmployeeDTO = modelMapper.map(savedEmployee, EmployeeDTO.class); // in case change the id, if we other test case which might change the id coz of auto increment

        // Modify the employee details
        testEmployeeDTO.setName("Random Name");
        testEmployeeDTO.setSalary(250_000L);
        log.info("Saved Employee: {}", savedEmployee);
        log.info("Test Employee DTO: {}", testEmployeeDTO);

        ApiResponse<EmployeeDTO> apiResponse = webTestClient.put()
                .uri("/employees/{employeeId}", savedEmployee.getEmployeeId())
                .bodyValue(testEmployeeDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<ApiResponse<EmployeeDTO>>() { // we convert the data using jackson
                })
                .returnResult()
                .getResponseBody();

        assert apiResponse != null;
        EmployeeDTO updatedEmployee = apiResponse.getData();
        log.info("Updated Employee: {}", updatedEmployee);

        assertThat(updatedEmployee).isEqualTo(testEmployeeDTO);


    }

    @Order(5)
    @Test
    void testUpdateEmployee_whenAttemptingToUpdateTheEmail_thenThrowException() {
        Employee savedEmployee = employeeRepository.save(testEmployee);
        testEmployeeDTO.setName("Random Name");
        testEmployeeDTO.setEmail("random@gmail.com");

        webTestClient.put()
                .uri("/employees/{employeeId}", savedEmployee.getEmployeeId())
                .bodyValue(EmployeeDTO.class)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Order(6)
    @Test
    void testUpdateEmployee_whenEmployeeDoesNotExists_thenThrowException() {
        webTestClient.put()
                .uri("/employees/999")
                .bodyValue(testEmployeeDTO)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Order(3)
    @Test
    void testUpdateEmployee_whenEmployeeIdDoesNotMatch_thenThrowsException() {

        // Set up an employee in the database
        Employee savedEmployee = employeeRepository.save(testEmployee);

        // Modify testEmployeeDTO to have a different ID than the one in the path
        testEmployeeDTO.setEmployeeId(savedEmployee.getEmployeeId() + 1); // mismatched ID

        webTestClient.put()
                .uri("/employees/{employeeId}", savedEmployee.getEmployeeId())
                .bodyValue(testEmployeeDTO)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Order(10)
    @Test
    void testDeleteEmployee_whenEmployeeExists_thenDeleteEmployee() {
        Employee savedEmployee = employeeRepository.save(testEmployee);

        webTestClient.delete()
                .uri("/employees/{employeeId}", savedEmployee.getEmployeeId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class);
/*
        * Delete the employee again, it should return 404
        webTestClient.delete()
                .uri("/employees/{employeeId}", savedEmployee.getEmployeeId())
                .exchange()
                .expectStatus().isNotFound();
*/
    }

    @Order(9)
    @Test
    void testDeleteEmployee_whenEmployeeDoesNotExists_thenThrowException() {
        webTestClient.delete()
                .uri("/employees/1")
                .exchange()
                .expectStatus().isNotFound();
    }
}