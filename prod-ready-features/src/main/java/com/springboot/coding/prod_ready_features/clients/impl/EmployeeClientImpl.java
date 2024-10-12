package com.springboot.coding.prod_ready_features.clients.impl;

import com.springboot.coding.prod_ready_features.advice.ApiResponse;
import com.springboot.coding.prod_ready_features.clients.EmployeeClient;
import com.springboot.coding.prod_ready_features.dto.EmployeeDTO;
import com.springboot.coding.prod_ready_features.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeClientImpl implements EmployeeClient {

    private final RestClient restClient;

    // Best Practice to write the log in the Service Layer
    private final Logger log = LoggerFactory.getLogger(EmployeeClientImpl.class);

    // Trace --> Information that is very detailed, Information about the flow through the system
    // Info --> Information that is helpful to the user
    // DEBUG --> Information that is helpful to developers
    // WARN --> Information that is not an error, but could be a problem
    // INFO --> Information about start and stop the service, connection, etc


    @Override
    public List<EmployeeDTO> getAllEmployees() { // List is Parameterized so we use in the body()

        // In Rest API side we have ApiResponse data, so it not work
//        try {
//
//            List<EmployeeDTO> employeeDTOList = restClient  // In --> URI append to the base URL
//                    .get()
//                    .uri("employees")
//                    .retrieve()
//                    .body(new ParameterizedTypeReference<>() {
//                    });
//            return employeeDTOList;
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//        log.error("error log");
//        log.warn("warn log");
//        log.info("info log");
//        log.debug("debug log");
//        log.trace("trace log");

        log.trace("Entered getAllEmployees");
        log.trace("Trying to retrieve the employees in getAllEmployees");
        try {
            log.info("Attempting to call the RestClient Method in getAllEmployees");
            ApiResponse<List<EmployeeDTO>> employeeDTOList = restClient  // In --> URI append to the base URL
                    .get()
                    .uri("employees")
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                        String error = new String(res.getBody().readAllBytes());
                        log.error("Client Error occurred: {}", error);
                        throw new ResourceNotFoundException("Could not get the employees");
                    })
                    .body(new ParameterizedTypeReference<>() {
                    });
            log.debug("Successfully retrieved the employees in getAllEmployees");

            List<EmployeeDTO> getEmployeeDTOList = List.of();
            if (employeeDTOList != null) {
                getEmployeeDTOList = employeeDTOList.getData();
                log.trace("Retrieve employees list in getAllEmployees: {}", getEmployeeDTOList);
            }
            log.trace("Exiting getAllEmployees");
            return getEmployeeDTOList;
//            return employeeDTOList != null ? employeeDTOList.getData() : List.of();
        } catch (Exception e) {
            // Error log
            log.error("Exception occurred in getAllEmployees: ", e);
            throw new RuntimeException(e);
        }
    }


    @Override
    public EmployeeDTO getEmployeeById(Long employeeId) {
        log.trace("Trying to get Employee By Id in getEmployeeById with id: {}", employeeId);
        try {
            ApiResponse<EmployeeDTO> employeeDTOApiResponse = restClient
                    .get()
//                    .uri("employees/" + employeeId)
                    .uri("employees/{employeeId}", employeeId)
//                    .uri("employees/{employeeId}/{abc}", employeeId, abc)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                        log.error(new String(res.getBody().readAllBytes()));
                        throw new ResourceNotFoundException("could not get the employee");
                    })
                    .body(new ParameterizedTypeReference<>() {
                    });
//            return employeeDTOApiResponse != null ? employeeDTOApiResponse.getData() : null;
            EmployeeDTO employeeDTO = null;
            if (employeeDTOApiResponse != null) {
                employeeDTO = employeeDTOApiResponse.getData();
                log.trace("Retrieve employee with employeeId: {} --- EmployeeDTO: {}", employeeId, employeeDTO);
            }
            return employeeDTO;
        } catch (Exception e) {
            log.error("Exception occurred in getEmployeeById: ", e);
            throw new RuntimeException(e);
        }
    }

    // While we create the Employee we might create an error so its on server side
    @Override
    public EmployeeDTO createNewEmployee(EmployeeDTO employeeDTO) {
//        try {
//            ApiResponse<EmployeeDTO> employeeDTOApiResponse = restClient
//                    .post()
//                    .uri("employees")
//                    .body(employeeDTO)
//                    .retrieve()
//                    .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
//                        String error = new String(res.getBody().readAllBytes());
//
//                        System.out.println("Client Error occurred: " + error);
//                        throw new RuntimeException("Could not create the employee");
//                        // throw new RuntimeException("Client Error");
//                    })
//                    .body(new ParameterizedTypeReference<>() {
//                    });
//            return employeeDTOApiResponse != null ? employeeDTOApiResponse.getData() : null;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }

        log.trace("Trying to create Employee with information {}", employeeDTO);
        try {
            ResponseEntity<ApiResponse<EmployeeDTO>> employeeDTOApiResponse = restClient
                    .post().uri("employees")
                    .body(employeeDTO)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                        log.debug("4xxClient error occurred during createNewEmployee");
                        String error = new String(res.getBody().readAllBytes());
                        log.error("Client Error occurred in createNewEmployee: {}", error);
                        throw new RuntimeException("Could not create the employee");
                        // throw new RuntimeException("Client Error");
                    })
                    .toEntity(new ParameterizedTypeReference<>() {
                    });
            ApiResponse<EmployeeDTO> employeeDTOApiResponseBody = employeeDTOApiResponse.getBody();
            if (employeeDTOApiResponseBody != null) {
                EmployeeDTO employee = employeeDTOApiResponseBody.getData();
                log.trace("Successfully created a new employee : {}", employee);
                return employee;
            }
//            return employeeDTOApiResponse.getBody() != null ? employeeDTOApiResponse.getBody().getData() : null;
            return null;
        } catch (Exception e) {
            log.error("Exception occurred in createNewEmployee", e);
            throw new RuntimeException(e);
        }
    }
}
