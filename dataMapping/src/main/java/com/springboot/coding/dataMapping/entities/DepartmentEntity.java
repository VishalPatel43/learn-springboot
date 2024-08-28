package com.springboot.coding.dataMapping.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.Set;


// One Department can have One Manager
// One Department can have Many Employees (Worker)
// One Department can have Many Freelancers

//@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "departments")
public class DepartmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long departmentId;

    @Column(nullable = false)
    private String title;

    // This side is the owner of the relationship
    // Here we have a foreign key column
    // Default name of column --> manager_employee_id (employee_id --> from EmployeeEntity)
    @OneToOne
    @JoinColumn(name = "department_manager") // change the default name of the column
    private EmployeeEntity manager;

    // One department can have many employees
    @OneToMany(mappedBy = "workerDepartment", fetch = FetchType.LAZY)
    private Set<EmployeeEntity> workers; // Unique set of employees

    @ManyToMany(mappedBy = "freelanceDepartments")
    private Set<EmployeeEntity> freelancers;

    @Override
    public String toString() {
        return "DepartmentEntity{" +
                "departmentId=" + departmentId +
                ", title='" + title + '\'' +
                ", workers=" + workers +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DepartmentEntity that)) return false;
        return Objects.equals(departmentId, that.departmentId) && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departmentId, title);
    }
}

