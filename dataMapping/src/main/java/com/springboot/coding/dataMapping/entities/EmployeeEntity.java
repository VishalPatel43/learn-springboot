package com.springboot.coding.dataMapping.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.Set;


// One Employee can be a worker in One Department
// One Employee can be a manager in One Department
// One Employee can be a freelancer in Many Departments

// But In which side we define the @JoinColumn, @JoinTable that side is the owner of the relationship and save entity
// not on mappedBy side
//@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "employees")
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    @Column(nullable = false)
    private String name;

    // Using this, we have bidirectional relationship
    // We access the manager of the department from the employee
    // We access the department of the manager from the employee
    // It will not create a column in the "employees" table
    @OneToOne(mappedBy = "manager") // manager is the field name in DepartmentEntity --> private EmployeeEntity manager;
    @JsonIgnore
    private DepartmentEntity managedDepartment;

    // Many employees can work in one department
    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
//    @JoinColumn(name = "worker_department_id", referencedColumnName = "departmentId")
    // create the new table
//    @JoinTable(name = "worker_department_mapping")
    @JoinTable(
            name = "worker_department_mapping",
            joinColumns = @JoinColumn(name = "employee_id"), // it will join with the employee_id column of the EmployeeEntity of id
            inverseJoinColumns = @JoinColumn(name = "worker_department_id", referencedColumnName = "departmentId") // it will join with the department_id column of the DepartmentEntity of id
    )
    private DepartmentEntity workerDepartment;

    // No owner of the relationship
    @ManyToMany
    @JoinTable(
            name = "freelancer_department_mapping",
            joinColumns = @JoinColumn(name = "employee_id"), // it will join with the employee_id column of the EmployeeEntity of id
            inverseJoinColumns = @JoinColumn(name = "department_id") // it will join with the department_id column of the DepartmentEntity of id
    )
    @JsonIgnore
    private Set<DepartmentEntity> freelanceDepartments;

    @Override
    public String toString() {
        return "EmployeeEntity{" +
                "employeeId=" + employeeId +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeEntity that = (EmployeeEntity) o;
        return Objects.equals(employeeId, that.employeeId) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId, name);
    }
}
