package com.springboot.coding.prod_ready_features.clients.impl;

import com.springboot.coding.prod_ready_features.advice.ApiResponse;
import com.springboot.coding.prod_ready_features.clients.EmployeeClient;
import com.springboot.coding.prod_ready_features.dto.EmployeeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeClientImpl implements EmployeeClient {

    private final RestClient restClient;

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

        try {
            ApiResponse<List<EmployeeDTO>> employeeDTOList = restClient  // In --> URI append to the base URL
                    .get()
                    .uri("employees")
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });
            return employeeDTOList != null ? employeeDTOList.getData() : List.of();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public EmployeeDTO getEmployeeById(Long employeeId) {
        try {
            ApiResponse<EmployeeDTO> employeeDTOApiResponse = restClient
                    .get()
//                    .uri("employees/" + employeeId)
                    .uri("employees/{employeeId}", employeeId)
//                    .uri("employees/{employeeId}/{abc}", employeeId, abc)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });
            return employeeDTOApiResponse != null ? employeeDTOApiResponse.getData() : null;
        } catch (Exception e) {
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

        try {
            ResponseEntity<ApiResponse<EmployeeDTO>> employeeDTOApiResponse = restClient
                    .post().uri("employees")
                    .body(employeeDTO)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                        String error = new String(res.getBody().readAllBytes());

                        System.out.println("Client Error occurred: " + error);
                        throw new RuntimeException("Could not create the employee");
                        // throw new RuntimeException("Client Error");
                    })
                    .toEntity(new ParameterizedTypeReference<>() {
                    });
            return employeeDTOApiResponse.getBody() != null ? employeeDTOApiResponse.getBody().getData() : null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
