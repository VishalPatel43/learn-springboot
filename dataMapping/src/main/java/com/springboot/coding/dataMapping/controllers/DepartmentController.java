package com.springboot.coding.dataMapping.controllers;

import com.springboot.coding.dataMapping.entities.DepartmentEntity;
import com.springboot.coding.dataMapping.services.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping(path = "/{departmentId}")
    public DepartmentEntity getDepartmentById(@PathVariable Long departmentId) {
        return departmentService.getDepartmentById(departmentId);
    }

    @PostMapping
    public DepartmentEntity createNewDepartment(@RequestBody DepartmentEntity departmentEntity) {
        return departmentService.createNewDepartment(departmentEntity);
    }

    // Assign Manager to Department
    @PutMapping(path = "/{departmentId}/manager/{employeeId}")
    public DepartmentEntity assignManagerToDepartment(@PathVariable Long departmentId,
                                                      @PathVariable Long employeeId) {
        return departmentService.assignManagerToDepartment(departmentId, employeeId);
    }

    @GetMapping(path = "assignedDepartmentOfManager/{employeeId}")
    public DepartmentEntity getAssignedDepartmentOfManager(@PathVariable Long employeeId) {
        return departmentService.getAssignedDepartmentOfManager(employeeId);
    }

    // Assign Worker to Department
    @PutMapping(path = "/{departmentId}/worker/{employeeId}")
    public DepartmentEntity assignWorkerToDepartment(@PathVariable Long departmentId,
                                                      @PathVariable Long employeeId) {
        return departmentService.assignWorkerToDepartment(departmentId, employeeId);
    }

    // Assign Freelancer to Department
    @PutMapping(path = "/{departmentId}/freelancers/{employeeId}")
    public DepartmentEntity assignFreelancerToDepartment(@PathVariable Long departmentId,
                                                     @PathVariable Long employeeId) {
        return departmentService.assignFreelancerToDepartment(departmentId, employeeId);
    }
}
