package com.springboot.coding.testingApp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {

    private Long employeeId;
    private String email;
    private String name;
    private Long salary;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmployeeDTO that)) return false;
        return Objects.equals(getEmployeeId(), that.getEmployeeId()) &&
                Objects.equals(getEmail(), that.getEmail()) &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getSalary(), that.getSalary());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmployeeId(), getEmail(), getName(), getSalary());
    }
}
