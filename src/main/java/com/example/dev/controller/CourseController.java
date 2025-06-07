package com.example.dev.controller;

import com.example.dev.dto.request.CourseDTO;
import com.example.dev.entity.Course;
import com.example.dev.repository.CourseRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseRepository courseRepo;

    public CourseController(CourseRepository courseRepo) {
        this.courseRepo = courseRepo;
    }

    @PostMapping
    public ResponseEntity<CourseDTO> createCourse(@RequestBody CourseDTO dto) {
        Course course = new Course();
        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        Course saved = courseRepo.save(course);
        return ResponseEntity.ok(toDTO(saved));
    }

    @GetMapping
    public List<CourseDTO> getAllCourses() {
        return courseRepo.findAll().stream().map(this::toDTO).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourse(@PathVariable Long id) {
        return courseRepo.findById(id)
                .map(this::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable Long id, @RequestBody CourseDTO dto) {
        return courseRepo.findById(id).map(course -> {
            course.setTitle(dto.getTitle());
            course.setDescription(dto.getDescription());
            return ResponseEntity.ok(toDTO(courseRepo.save(course)));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        if (!courseRepo.existsById(id)) return ResponseEntity.notFound().build();
        courseRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private CourseDTO toDTO(Course course) {
        CourseDTO dto = new CourseDTO();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        return dto;
    }
}


