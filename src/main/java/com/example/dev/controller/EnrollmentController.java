package com.example.dev.controller;

import com.example.dev.dto.request.EnrollmentRequest;
import com.example.dev.dto.response.EnrollmentDTO;
import com.example.dev.entity.Enrollment;
import com.example.dev.service.EnrollmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping
    public ResponseEntity<String> enrollStudent(@RequestBody EnrollmentRequest request) {
        enrollmentService.enrollStudent(request.getStudentId(), request.getCourseId(), request.getGrade());
        return ResponseEntity.ok("Student enrolled successfully.");
    }

    @GetMapping("/student/{studentId}")
    public List<EnrollmentDTO> getEnrollmentsByStudent(@PathVariable Long studentId) {
        return enrollmentService.getEnrollmentDTOsByStudentId(studentId);
    }



}

