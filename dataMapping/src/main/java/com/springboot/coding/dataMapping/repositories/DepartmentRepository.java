package com.springboot.coding.dataMapping.repositories;

import com.springboot.coding.dataMapping.entities.DepartmentEntity;
import com.springboot.coding.dataMapping.entities.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long> {

    DepartmentEntity findByManager(EmployeeEntity employeeEntity);
}
