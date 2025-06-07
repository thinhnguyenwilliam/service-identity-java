package com.example.dev.service;

import com.example.dev.dto.request.DepartmentDTO;
import com.example.dev.entity.Department;
import com.example.dev.mapper.DepartmentMapper;
import com.example.dev.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepo;
    private final DepartmentMapper mapper;

    public DepartmentService(DepartmentRepository departmentRepo, DepartmentMapper mapper) {
        this.departmentRepo = departmentRepo;
        this.mapper = mapper;
    }

    public DepartmentDTO createDepartment(DepartmentDTO dto) {
        Department department = mapper.toEntity(dto);

        // Set the back-reference (very important in bidirectional relationships)
        if (department.getEmployees() != null) {
            department.getEmployees().forEach(emp -> emp.setDepartment(department));
        }

        Department saved = departmentRepo.save(department);
        return mapper.toDTO(saved);
    }

    public DepartmentDTO getDepartmentById(Long id) {
        Department department = departmentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));
        return mapper.toDTO(department);
    }
}


