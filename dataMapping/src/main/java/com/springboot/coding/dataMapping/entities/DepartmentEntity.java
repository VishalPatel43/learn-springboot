package com.springboot.coding.dataMapping.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


// One Department can have One Manager
// One Department can have Many Employees (Worker)

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "departments")
public class DepartmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long department_id;

    @Column(nullable = false)
    private String title;

    // This side is the owner of the relationship
    // Here we have a foreign key column
    // Default name of column --> manager_employee_id (employee_id --> from EmployeeEntity)
    @OneToOne
    @JoinColumn(name = "department_manager") // change the default name of the column
    private EmployeeEntity manager;
}

