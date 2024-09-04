package com.springboot.coding.prod_ready_features;

import com.springboot.coding.prod_ready_features.clients.EmployeeClient;
import com.springboot.coding.prod_ready_features.dto.EmployeeDTO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProdReadyFeaturesApplicationTests {

    @Autowired
    private EmployeeClient employeeClient;

    @Test
    @Order(3)
    void getAllEmployees() {
        List<EmployeeDTO> employeeDTOList = employeeClient.getAllEmployees();

        for (EmployeeDTO employeeDTO : employeeDTOList)
            System.out.println(employeeDTO);
    }

    @Test
    @Order(2)
    void getEmployeeById() {
        EmployeeDTO employeeDTO = employeeClient.getEmployeeById(52L);
        System.out.println(employeeDTO);
    }

    @Test
    @Order(1)
    void createNewEmployee() {
//        EmployeeDTO employeeDTO = new EmployeeDTO(null, "abc", "abc@emial.com",
//                20, "USER", 100000.0,
//                LocalDate.of(2021, 1, 1),
//                true
//        );
        EmployeeDTO employeeDTO = new EmployeeDTO(null, "ccc", "ccc@email.com",
                2, "USER", 100000.0,
                LocalDate.of(2021, 1, 1),
                true
        );
        EmployeeDTO savedEmployeeDTO = employeeClient.createNewEmployee(employeeDTO);

        System.out.println(savedEmployeeDTO);
    }


}
