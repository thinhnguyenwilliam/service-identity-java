package com.example.dev.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentDTO {
    private Long studentId;
    private Long courseId;
    private String studentName;
    private String courseTitle;
    private String grade;
    private LocalDate enrollmentDate;
    // Getters and setters
}


