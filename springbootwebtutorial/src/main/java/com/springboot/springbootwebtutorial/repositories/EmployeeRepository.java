package com.springboot.springbootwebtutorial.repositories;

import com.springboot.springbootwebtutorial.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Persistent Layer or Repository Layer (Data Access Layer) or Data Access Object (DAO)
@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {
}
