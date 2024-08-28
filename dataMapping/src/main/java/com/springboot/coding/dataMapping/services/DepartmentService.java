package com.springboot.coding.dataMapping.services;

import com.springboot.coding.dataMapping.entities.DepartmentEntity;
import com.springboot.coding.dataMapping.entities.EmployeeEntity;
import com.springboot.coding.dataMapping.repositories.DepartmentRepository;
import com.springboot.coding.dataMapping.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository,
                             EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    public DepartmentEntity createNewDepartment(DepartmentEntity departmentEntity) {
        return departmentRepository.save(departmentEntity);
    }

    public DepartmentEntity getDepartmentById(Long id) {
        return departmentRepository.findById(id).orElse(null);
    }

    public DepartmentEntity assignManagerToDepartment(Long departmentId, Long employeeId) {
        Optional<DepartmentEntity> departmentEntity = departmentRepository.findById(departmentId);
        Optional<EmployeeEntity> employeeEntity = employeeRepository.findById(employeeId);

        return departmentEntity.flatMap( // return department entity
                        department -> employeeEntity.map( // if departmentEntity is present, then map employeeEntity
                                employee -> { // if both are present, then set the manager of the department to the employee
                                    department.setManager(employee); // set the manager of the department to the employee
                                    return departmentRepository.save(department); // save the department in the database and return department entity
                                }
                        )
                )
                .orElse(null);
    }

    public DepartmentEntity assignManagerToDepartment1(Long departmentId, Long employeeId) {
        // Step 1: Fetch the department by its ID
        Optional<DepartmentEntity> departmentOptional = departmentRepository.findById(departmentId);

        // Step 2: Check if the department exists
        if (departmentOptional.isEmpty())
            // Handle the case where the department does not exist
            throw new NoSuchElementException("Department with ID " + departmentId + " not found.");

        // Step 3: Fetch the employee by their ID
        Optional<EmployeeEntity> employeeOptional = employeeRepository.findById(employeeId);

        // Step 4: Check if the employee exists
        if (employeeOptional.isEmpty())
            // Handle the case where the employee does not exist
            throw new NoSuchElementException("Employee with ID " + employeeId + " not found.");

        // Step 5: Retrieve the department and employee entities from the optionals
        DepartmentEntity department = departmentOptional.get();
        EmployeeEntity employee = employeeOptional.get();

        // Step 6: Assign the employee as the manager of the department
        department.setManager(employee);

        // Step 7: Save the updated department entity to the database &  Return the updated department entity
        return departmentRepository.save(department);
    }


    public DepartmentEntity getAssignedDepartmentOfManager(Long employeeId) {
        // 1 --> Fetch the employee by its ID and return the department managed by the employee
        // One Database call
//        Optional<EmployeeEntity> employeeEntity = employeeRepository.findById(employeeId);
//        return employeeEntity
//                .map(EmployeeEntity::getManagedDepartment)
//                .orElse(null);
//        return employeeEntity
//                .map(employee -> employee.getManagedDepartment())
//                .orElse(null);

//        return employeeEntity
//                .map(employee -> {
//                    return employee.getManagedDepartment();
//                })
//                .orElse(null);
//        return employeeEntity.get().getManagedDepartment();

        // 2 --> Fetch the employee by its ID then using this employee, fetch the department managed by the employee
        // Two Database calls
//        Optional<EmployeeEntity> employeeEntity = employeeRepository.findById(employeeId);
//        if (employeeEntity.isEmpty())
//            throw new NoSuchElementException("Employee with ID " + employeeId + " not found.");
//
//        return departmentRepository.findByManager(employeeEntity.get());

//        In Reality, we just need Employee with EmployeeID not the entire EmployeeEntity
        // 3 --> Give Only EmployeeID with EmployeeEntity
        EmployeeEntity employeeEntity = EmployeeEntity
                .builder()
                .employeeId(employeeId)
                .build();

        // need only employeeId not the entire employeeEntity
        return departmentRepository.findByManager(employeeEntity);
    }

    public DepartmentEntity assignWorkerToDepartment(Long departmentId, Long employeeId) {

        Optional<DepartmentEntity> departmentEntity = departmentRepository.findById(departmentId);
        Optional<EmployeeEntity> employeeEntity = employeeRepository.findById(employeeId);

        // not reflect in the database
//        return departmentEntity.flatMap(
//                        department ->
//                                employeeEntity.map(employee -> {
        // It's not work coz its not owner of relationship
//                                            department.getWorkers().add(employee);
//                                            System.out.println("Department---> " + department);
//                                            return departmentRepository.save(department);
//                                        }
//                                )
//                )
//                .orElse(null);

        return departmentEntity.flatMap(department ->
                employeeEntity.map(employee -> {
                    employee.setWorkerDepartment(department);
                    employeeRepository.save(employee);
                    System.out.println("Department-> " + department);
                    department.getWorkers().add(employee); // we return this department
                    System.out.println("Department---> " + department);
                    return department;
                })).orElse(null);

        // 2
//        if (departmentEntityOptional.isPresent() && employeeEntityOptional.isPresent()) {
//            DepartmentEntity department = departmentEntityOptional.get();
//            EmployeeEntity employee = employeeEntityOptional.get();
//
//            // Update both sides of the relationship
//            employee.setWorkerDepartment(department);
//            department.getWorkers().add(employee); // which we return
//
//            // Save the employee first to reflect the changes in the join column
//            employeeRepository.save(employee);

//        return department;
//        }

//        return null;

    }

    public DepartmentEntity assignFreelancerToDepartment(Long departmentId, Long employeeId) {
        Optional<DepartmentEntity> departmentEntity = departmentRepository.findById(departmentId);
        Optional<EmployeeEntity> employeeEntity = employeeRepository.findById(employeeId);

        return departmentEntity.flatMap(department ->
                employeeEntity.map(employee -> {
                    // either we can go to department entity and save employee or employee entity and save department
                    // But In which side we define the @JoinColumn, @JoinTable that side is the owner of the relationship and save entity
                    // not on mappedBy side
//                    department.getFreelancers().add(employee);
//                    departmentRepository.save(department);
//                    return department;

                    employee.getFreelanceDepartments().add(department);
                    employeeRepository.save(employee);

                    // for return java object
                    department.getFreelancers().add(employee);
                    return department;
                })).orElse(null);
    }
}
