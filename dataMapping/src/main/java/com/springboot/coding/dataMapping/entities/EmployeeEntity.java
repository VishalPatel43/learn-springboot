package com.springboot.coding.dataMapping.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


// One Employee can be a worker in One Department
// One Employee can be a manager in One Department
// One Employee can work on many (multiple) Projects


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "employees")
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    @Column(nullable = false)
    private String name;

    // Using this we have bidirectional relationship
    // We access the manager of the department from the employee
    // We access the department of the manager from the employee
    @OneToOne(mappedBy = "manager") // manager is the field name in DepartmentEntity --> private EmployeeEntity manager;
    // It will not create column in the employees table
    private DepartmentEntity managedDepartment;
}
