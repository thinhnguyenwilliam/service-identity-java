package com.example.dev.service;

import com.example.dev.dto.request.EmployeeDTO;
import com.example.dev.entity.Department;
import com.example.dev.entity.Employee;
import com.example.dev.mapper.DepartmentMapper;
import com.example.dev.repository.DepartmentRepository;
import com.example.dev.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepo;
    private final DepartmentRepository departmentRepo;
    private final DepartmentMapper mapper;

    public EmployeeService(EmployeeRepository employeeRepo, DepartmentRepository departmentRepo, DepartmentMapper mapper) {
        this.employeeRepo = employeeRepo;
        this.departmentRepo = departmentRepo;
        this.mapper = mapper;
    }

    public EmployeeDTO createEmployee(EmployeeDTO dto) {
        Employee employee = mapper.toEntity(dto);

        // Assign department from departmentId
        Department dept = departmentRepo.findById(dto.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        employee.setDepartment(dept);

        Employee saved = employeeRepo.save(employee);
        return mapper.toDTO(saved);
    }
}

