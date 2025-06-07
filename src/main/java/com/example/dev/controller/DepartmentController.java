package com.example.dev.controller;

import com.example.dev.dto.request.DepartmentDTO;
import com.example.dev.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    public ResponseEntity<DepartmentDTO> createDepartment(@RequestBody DepartmentDTO dto) {
        DepartmentDTO saved = departmentService.createDepartment(dto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDTO> getDepartment(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.getDepartmentById(id));
    }
}


