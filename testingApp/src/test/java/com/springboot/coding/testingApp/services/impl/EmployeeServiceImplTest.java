package com.springboot.coding.testingApp.services.impl;

import com.springboot.coding.testingApp.TestContainerConfiguration;
import com.springboot.coding.testingApp.dto.EmployeeDTO;
import com.springboot.coding.testingApp.entities.Employee;
import com.springboot.coding.testingApp.exceptions.ResourceNotFoundException;
import com.springboot.coding.testingApp.repositories.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;


//@SpringBootTest // load entire spring context --> web server, database, etc., we just want the unit testing not integrated testing, so we use the mocking
// not replace the database and take the TestContainerConfiguration database
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Slf4j
@Import(TestContainerConfiguration.class)
@ExtendWith(MockitoExtension.class) // no need to use the @SpringBootTest
class EmployeeServiceImplTest {

    // EmployeeRepository and ModelMapper are dependencies of EmployeeServiceImpl, so write both
    @Mock
    private EmployeeRepository employeeRepository;

    //    @Mock
    @Spy // --> it will create the real object, and we can use the real object
    private ModelMapper modelMapper;

    // @Autowired --> we can't use this coz we don't use the @SpringBootTest or @DataJpaTest
    @InjectMocks
//    private EmployeeService employeeService; // --> it will not create the bean so we have to use the concrete class
    private EmployeeServiceImpl employeeService;


    private Employee mockEmployee;
    private EmployeeDTO mockEmployeeDTO;

    @BeforeEach
    void setUp() {
        Long employeeId = 1L;

        mockEmployee = Employee.builder()
                .employeeId(employeeId)
                .name("Vishal")
                .email("v@email.com")
                .salary(200_000L)
                .build();

        mockEmployeeDTO = modelMapper.map(mockEmployee, EmployeeDTO.class);

    }

    @Test
    void testGetEmployeeById_WhenEmployeeIdIsPresent_ThenReturnEmployeeDTO() {

        // Assign (Assert)
        Long employeeId = mockEmployee.getEmployeeId();

        // stubbing --> when the findById method is called with employeeId then return the mockEmployee
        // It's not real employeeRepository, it's a mock object
        // When service calls the findById method, it will return the mockEmployee
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(mockEmployee)); // also assign the data in database
        // employeeRepository.findById(employeeId)) --> when this method calls we return the mockEmployee

        // Act (When)
        // test on actual service method
        EmployeeDTO employeeDTO = employeeService.getEmployeeDTOById(employeeId);
        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        log.info("Employee: {}", employee);
        log.info("EmployeeDTO: {}", employeeDTO);

        // Assert (Then)
        assertThat(employeeDTO).isNotNull();
        assertThat(employeeDTO.getEmployeeId()).isEqualTo(mockEmployee.getEmployeeId());
        assertThat(employeeDTO.getEmail()).isEqualTo(mockEmployee.getEmail());
        verify(employeeRepository, times(2)).findById(employeeId);
//        verify(employeeRepository, only()).findById(employeeId);
    }


    @Test
    void testGetEmployeeById_whenEmployeeIsNotPresent_thenThrowException() {
        // assign
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

        // act and assert
        assertThatThrownBy(() -> employeeService.getEmployeeDTOById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: 1");

        verify(employeeRepository).findById(1L);
    }

    @Test
    void testCreateNewEmployee_WhenValidEmployee_ThenCreateNewEmployee() {
        // assign
        // if it's find any email (any string) then return Option empty, it will not throw exception, coz employee is not present
        when(employeeRepository.findByEmail(anyString())).thenReturn(Optional.empty());
//        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(employeeRepository.save(any(Employee.class))).thenReturn(mockEmployee); // stubbing

        // act
        EmployeeDTO employeeDTO = employeeService.createNewEmployee(mockEmployeeDTO);

        // assert

        assertThat(employeeDTO).isNotNull();
        assertThat(employeeDTO.getEmail()).isEqualTo(mockEmployee.getEmail());

//        verify(employeeRepository).save(any(Employee.class));
        // Create an ArgumentCaptor for Employee
        ArgumentCaptor<Employee> employeeArgumentCaptor = ArgumentCaptor.forClass(Employee.class);

        // Verify the save method was called and capture the argument
        verify(employeeRepository).save(employeeArgumentCaptor.capture());

        // Retrieve the captured Employee object
        Employee capturedEmployee = employeeArgumentCaptor.getValue();

        // Perform assertions on the captured argument
        assertThat(capturedEmployee.getEmail()).isEqualTo(mockEmployee.getEmail());
    }
}