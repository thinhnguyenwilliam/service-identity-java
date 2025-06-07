package com.example.dev.controller;


import com.example.dev.dto.request.CourseDTO;
import com.example.dev.dto.request.StudentDTO;
import com.example.dev.entity.Course;
import com.example.dev.entity.Student;
import com.example.dev.repository.StudentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentRepository studentRepo;

    public StudentController(StudentRepository studentRepo) {
        this.studentRepo = studentRepo;
    }

    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(@RequestBody StudentDTO dto) {
        Student student = new Student();
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        Student saved = studentRepo.save(student);
        return ResponseEntity.ok(toDTO(saved));
    }

    @GetMapping
    public List<StudentDTO> getAllStudents() {
        return studentRepo.findAll().stream().map(this::toDTO).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudent(@PathVariable Long id) {
        return studentRepo.findById(id)
                .map(this::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable Long id, @RequestBody StudentDTO dto) {
        return studentRepo.findById(id).map(student -> {
            student.setName(dto.getName());
            student.setEmail(dto.getEmail());
            return ResponseEntity.ok(toDTO(studentRepo.save(student)));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        if (!studentRepo.existsById(id)) return ResponseEntity.notFound().build();
        studentRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private StudentDTO toDTO(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setName(student.getName());
        dto.setEmail(student.getEmail());

        // collect courses from enrollments
        List<CourseDTO> courses = student.getEnrollments().stream()
                .map(enrollment -> {
                    Course course = enrollment.getCourse();
                    CourseDTO courseDTO = new CourseDTO();
                    courseDTO.setId(course.getId());
                    courseDTO.setTitle(course.getTitle());
                    courseDTO.setDescription(course.getDescription());
                    return courseDTO;
                })
                .toList();

        dto.setCourses(courses);

        return dto;
    }

}

