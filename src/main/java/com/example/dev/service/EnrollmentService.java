package com.example.dev.service;

import com.example.dev.dto.response.EnrollmentDTO;
import com.example.dev.entity.Course;
import com.example.dev.entity.Enrollment;
import com.example.dev.entity.Student;
import com.example.dev.repository.CourseRepository;
import com.example.dev.repository.EnrollmentRepository;
import com.example.dev.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {

    private final StudentRepository studentRepo;
    private final CourseRepository courseRepo;
    private final EnrollmentRepository enrollmentRepo;

    public EnrollmentService(StudentRepository studentRepo,
                             CourseRepository courseRepo,
                             EnrollmentRepository enrollmentRepo) {
        this.studentRepo = studentRepo;
        this.courseRepo = courseRepo;
        this.enrollmentRepo = enrollmentRepo;
    }

    public void enrollStudent(Long studentId, Long courseId, String grade) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student ID"));

        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course ID"));

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setGrade(grade);

        enrollmentRepo.save(enrollment);
    }

    public List<EnrollmentDTO> getEnrollmentDTOsByStudentId(Long studentId) {
        return enrollmentRepo.findAll().stream()
                .filter(e -> e.getStudent().getId().equals(studentId))
                .map(e -> {
                    EnrollmentDTO dto = new EnrollmentDTO();
                    dto.setStudentId(e.getId().getStudentId());
                    dto.setCourseId(e.getId().getCourseId());
                    dto.setStudentName(e.getStudent().getName());
                    dto.setCourseTitle(e.getCourse().getTitle());
                    dto.setGrade(e.getGrade());
                    dto.setEnrollmentDate(e.getEnrollmentDate());
                    return dto;
                })
                .toList();
    }

}

