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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


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

    // For Success Scenario
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


    // For Failure Scenario
    @Test
    void testGetEmployeeById_whenEmployeeIsNotPresent_thenThrowException() {
        // assign
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty()); // we set the not present, so it will throw the exception

        // act and assert
        assertThatThrownBy(() -> employeeService.getEmployeeDTOById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with ID: 1");

        verify(employeeRepository).findById(1L);
    }

    // For Success Scenario
    @Test
    void testCreateNewEmployee_WhenValidEmployee_ThenCreateNewEmployee() {
        // assign
        // if it's find any email (any string) then return Option empty, it will not throw exception, coz employee is not present
        when(employeeRepository.findByEmail(anyString())).thenReturn(Optional.empty()); // employee is not present so we can create the new employee
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
        // after the save method is called, we can capture the argument
//        log.info("Get Employee : {}", employeeArgumentCaptor.getValue());

        // Retrieve the captured Employee object
        Employee capturedEmployee = employeeArgumentCaptor.getValue();
        log.info("Captured Employee: {}", capturedEmployee);

        // Perform assertions on the captured argument
        assertThat(capturedEmployee.getEmail()).isEqualTo(mockEmployee.getEmail());
    }

    // For Failure Scenario
    @Test
    void testCreateNewEmployee_whenAttemptingToCreateEmployeeWithExistingEmail_thenThrowException() {

        // arrange
//        when(employeeRepository.findByEmail(anyString())).thenReturn(Optional.of(mockEmployee));
        when(employeeRepository.findByEmail(mockEmployeeDTO.getEmail())).thenReturn(Optional.of(mockEmployee));

        // act and assert
        assertThatThrownBy(() -> employeeService.createNewEmployee(mockEmployeeDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Employee already exists with email: " + mockEmployee.getEmail());

//        verify(employeeRepository).findByEmail(anyString());
        verify(employeeRepository).findByEmail(mockEmployeeDTO.getEmail());
        verify(employeeRepository, never()).save(any()); // it will not call the save method coz it will throw the exception
    }

    // For Success Scenario
    @Test
    void testUpdateEmployee_whenValidEmployee_thenUpdateEmployee() {

        // arrange
        when(employeeRepository.findById(mockEmployeeDTO.getEmployeeId())).thenReturn(Optional.of(mockEmployee)); // we set the employee is present
        mockEmployeeDTO.setName("Random Name");
        mockEmployeeDTO.setSalary(300_000L);

        Employee employee = modelMapper.map(mockEmployeeDTO, Employee.class);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        // act
        EmployeeDTO updatedEmployeeDTO = employeeService.updateEmployee(mockEmployeeDTO.getEmployeeId(), mockEmployeeDTO);

        // assert
        assertThat(updatedEmployeeDTO).isNotNull();
        assertThat(updatedEmployeeDTO).isEqualTo(mockEmployeeDTO); // we compare EmployeeDTO objects so write the equals method in EmployeeDTO class

        verify(employeeRepository).findById(1L);
        verify(employeeRepository).save(any(Employee.class));
        verify(employeeRepository).save(any());
    }

    // For Failure Scenario
    @Test
    void testUpdateEmployee_whenEmployeeDoesNotExists_thenThrowException() {
//        arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

//        act and assert
        assertThatThrownBy(() -> employeeService.updateEmployee(1L, mockEmployeeDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with ID: 1");

        verify(employeeRepository).findById(1L);
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void testUpdateEmployee_whenEmployeeIdDoesNotMatch_thenThrowsException() {
        // arrange
        Long employeeId = 2L;
        Employee randomEmployee = Employee.builder()
                .employeeId(employeeId)
                .name("Random")
                .email("random@email.com")
                .salary(200_000L)
                .build();

        when(employeeRepository.findById(randomEmployee.getEmployeeId())).thenReturn(Optional.of(randomEmployee));

        // act and assert
        assertThatThrownBy(() -> employeeService.updateEmployee(employeeId, mockEmployeeDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The provided EmployeeDTO ID does not match the target employeeId.");

        verify(employeeRepository).findById(randomEmployee.getEmployeeId());
        assertThat(randomEmployee.getEmployeeId()).isNotEqualTo(mockEmployeeDTO.getEmployeeId());
        verify(employeeRepository, never()).save(any());
    }

    // For Failure Scenario
    @Test
    void testUpdateEmployee_whenAttemptingToUpdateEmail_thenThrowException() {
        when(employeeRepository.findById(mockEmployeeDTO.getEmployeeId())).thenReturn(Optional.of(mockEmployee));
        mockEmployeeDTO.setName("Random");
        mockEmployeeDTO.setEmail("random@gmail.com");

//        act and assert
        assertThatThrownBy(() -> employeeService.updateEmployee(mockEmployeeDTO.getEmployeeId(), mockEmployeeDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("The email of the employee cannot be updated");

        verify(employeeRepository).findById(mockEmployeeDTO.getEmployeeId());
        verify(employeeRepository, never()).save(any());
    }

    // For Success Scenario
    @Test
    void testDeleteEmployee_whenEmployeeIsValid_thenDeleteEmployee() {
//        arrange
        when(employeeRepository.existsById(1L)).thenReturn(true);

        // act

        // We also check the code is not throwing any exception
        assertThatCode(() -> employeeService.deleteEmployee(1L))
                .doesNotThrowAnyException();

        verify(employeeRepository).deleteById(1L);
    }

    // For Failure Scenario
    @Test
    void testDeleteEmployee_whenEmployeeDoesNotExists_thenThrowException() {
        when(employeeRepository.existsById(1L)).thenReturn(false);

//        act
        assertThatThrownBy(() -> employeeService.deleteEmployee(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with employeeId: " + 1L);

        verify(employeeRepository, never()).deleteById(anyLong());
    }
}